/*
 * Copyright 2020 LinkedIn Corp. Licensed under the BSD 2-Clause License (the "License"). See License in the project root for license information.
 */

package com.linkedin.kafka.cruisecontrol.servlet.security.spnego;

import com.linkedin.kafka.cruisecontrol.servlet.security.MiniKdc;
import com.linkedin.kafka.cruisecontrol.CruiseControlIntegrationTestHarness;
import com.linkedin.kafka.cruisecontrol.config.constants.WebServerConfig;
import org.apache.kerby.kerberos.kerb.KrbException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.net.URI;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.linkedin.kafka.cruisecontrol.servlet.CruiseControlEndPoint.STATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SpnegoSecurityProviderIntegrationTest extends CruiseControlIntegrationTestHarness {

  private static final String CRUISE_CONTROL_STATE_ENDPOINT = "kafkacruisecontrol/" + STATE;

  private static final String CC_TEST_ADMIN = "ccTestAdmin";
  private static final String REALM = "LINKEDINTEST.COM";
  private static final String CLIENT_PRINCIPAL = CC_TEST_ADMIN + "/localhost";
  private static final String SOME_OTHER_SERVICE_PRINCIPAL = "someotherservice/localhost";
  private static final String SPNEGO_SERVICE_PRINCIPAL = "HTTP/localhost";

  private MiniKdc _miniKdc;

  public SpnegoSecurityProviderIntegrationTest() throws KrbException {
    List<String> principals = new ArrayList<>();
    principals.add(CLIENT_PRINCIPAL);
    principals.add(SPNEGO_SERVICE_PRINCIPAL);
    principals.add(SOME_OTHER_SERVICE_PRINCIPAL);
    _miniKdc = new MiniKdc(REALM, principals);
  }

  @Override
  protected Map<String, Object> withConfigs() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(WebServerConfig.WEBSERVER_SECURITY_ENABLE_CONFIG, true);
    configs.put(WebServerConfig.WEBSERVER_SECURITY_PROVIDER_CONFIG, SpnegoSecurityProvider.class);
    configs.put(WebServerConfig.WEBSERVER_AUTH_CREDENTIALS_FILE_CONFIG,
        Objects.requireNonNull(this.getClass().getClassLoader().getResource("auth.credentials")).getPath());
    configs.put(WebServerConfig.SPNEGO_PRINCIPAL_CONFIG, SPNEGO_SERVICE_PRINCIPAL + "@" + REALM);
    configs.put(WebServerConfig.SPNEGO_KEYTAB_FILE_CONFIG, _miniKdc.keytab().getAbsolutePath());

    return configs;
  }

  /**
   * Initializes the test environment.
   * @throws Exception
   */
  @Before
  public void setup() throws Exception {
    _miniKdc.start();
    super.start();
  }

  /**
   * Stops the test environment.
   * @throws KrbException
   */
  @After
  public void teardown() throws KrbException {
    super.stop();
    _miniKdc.stop();
  }

  @Test
  public void testSuccessfulAuthentication() throws Exception {
    Subject subject = _miniKdc.loginAs(CLIENT_PRINCIPAL);
    Subject.doAs(subject, (PrivilegedAction<Object>) () -> {
      try {
        HttpURLConnection stateEndpointConnection = (HttpURLConnection) new URI(_app.serverUrl())
            .resolve(CRUISE_CONTROL_STATE_ENDPOINT).toURL().openConnection();
        assertEquals(HttpServletResponse.SC_OK, stateEndpointConnection.getResponseCode());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return null;
    });
  }

  @Test
  public void testNotAdminServiceLogin() throws Exception {
    Subject subject = _miniKdc.loginAs(SOME_OTHER_SERVICE_PRINCIPAL);
    Subject.doAs(subject, (PrivilegedAction<Object>) () -> {
      HttpURLConnection stateEndpointConnection;
      try {
        stateEndpointConnection = (HttpURLConnection) new URI(_app.serverUrl())
            .resolve(CRUISE_CONTROL_STATE_ENDPOINT).toURL().openConnection();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      // There is a bug in the Jetty implementation and it doesn't seem to handle the connection
        // properly in case of an error so it somehow doesn't send a response code. To work this around
        // I catch the RuntimeException that it throws.
        assertThrows(RuntimeException.class, stateEndpointConnection::getResponseCode);
      return null;
    });
  }
}
