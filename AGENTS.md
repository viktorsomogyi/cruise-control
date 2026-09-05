# Repository guide for agents

Guide for coding agents that work in the Cruise Control for Kafka repository.

This document uses these requirement levels:

| Keyword            | How to treat it           |
| ------------------ | ------------------------- |
| MUST/REQUIRED      | Mandatory                 |
| SHOULD/RECOMMENDED | Deviate only with reason  |
| MAY/OPTIONAL       | Use judgment              |

Follow project conventions first, then this guide. The style rules are adapted from the [Google developer documentation style guide](https://developers.google.com/style). If something is not covered here, look it up there. Prefer a clear sentence over a strictly compliant one. When you depart from the guide, stay consistent in that file or pull request.

The full contribution policy is in [CONTRIBUTING.md](./CONTRIBUTING.md). This file tells you how to apply that policy when you write code, commits, and pull requests.

## Repository structure

Cruise Control is a Gradle multi-module Java project that balances and heals Apache Kafka clusters. Java 17 is required.

| Path | What it contains |
| ---- | ---------------- |
| `cruise-control/` | Server: REST API, analyzer, executor, load monitor, anomaly detectors, and security. |
| `cruise-control-core/` | Shared types: metric aggregation, config, HTTP, and anomaly interfaces. |
| `cruise-control-metrics-reporter/` | Broker `MetricsReporter` that publishes samples to Kafka. |
| `cruise-control-client/` | Python client. |
| `config/` | Sample server, capacity, and related configuration files. |
| `docs/wiki/` | Architecture, REST API, configuration, and developer guides. |
| `docs/code-style.xml` | IntelliJ code style. |
| `checkstyle/` | Checkstyle config and the optional pre-commit hook. |
| `.github/pull_request_template.md` | Required pull request template. |

## On-demand documentation

If the task touches an area in the following table, read that document first.

| Document | Read it when you need |
| -------- | --------------------- |
| [README.md](./README.md) | Build, run, and feature overview. |
| [CONTRIBUTING.md](./CONTRIBUTING.md) | License headers, Developer Certificate of Origin (DCO), pull request rules, and change size. |
| [CHARTER.md](./CHARTER.md) | Project license and DCO requirements in the charter. |
| [GOVERNANCE.md](./GOVERNANCE.md) | Technical Steering Committee (TSC), maintainers, and voting. |
| [SECURITY.md](./SECURITY.md) | How to report a vulnerability. Don't file security issues as public GitHub issues. |
| [docs/wiki/Overview.md](./docs/wiki/Overview.md) | Architecture: load monitor, analyzer, executor, and anomaly detector. |
| [docs/wiki/User Guide/REST-APIs.md](./docs/wiki/User%20Guide/REST-APIs.md) | HTTP endpoints and parameters. |
| [docs/wiki/User Guide/Configurations.md](./docs/wiki/User%20Guide/Configurations.md) | Server and component configuration keys. |
| [docs/wiki/User Guide/Pluggable-Components.md](./docs/wiki/User%20Guide/Pluggable-Components.md) | Goals, metric sampler, sample store, and anomaly notifier. |
| [docs/wiki/User Guide/Security.md](./docs/wiki/User%20Guide/Security.md) | Authentication and authorization. |
| [docs/wiki/Developer Guide/Write-your-own-goals.md](./docs/wiki/Developer%20Guide/Write-your-own-goals.md) | How to implement a `Goal`. |
| [docs/wiki/Developer Guide/Build-the-cluster-workload-model.md](./docs/wiki/Developer%20Guide/Build-the-cluster-workload-model.md) | How replica-level load is derived from broker metrics. |
| [docs/wiki/Troubleshooting.md](./docs/wiki/Troubleshooting.md) | Common operational failures. |

## Contribution rules

You MUST follow these rules when you change this repository.

### Tests

Treat tests as part of the change, not a follow-up:

- New features MUST include passing tests. Run both existing tests and the new ones before you open a pull request.
- Bug fixes MUST include a test that fails without the fix and passes with it.

### Issues and change size

Scope the work before you open a pull request:

- If the change is large or has never been discussed, open an issue first and get agreement before you write a pull request. Undiscussed large features are unlikely to be accepted.
- The following are major changes: configuration, tooling, UI, REST API, Java interfaces, and the protocol between the metric reporter and Cruise Control. A major change needs a design review and a [consensus vote](./GOVERNANCE.md#voting-process). Submit the design to the proposals repository in the Cruise Control for Kafka GitHub organization.
- Each pull request MUST link to an existing issue. If no issue exists, create one first.

### License headers

New and edited source files MUST include the Apache 2.0 header from [CONTRIBUTING.md](./CONTRIBUTING.md). Files that still carry the original LinkedIn/BSD header MUST keep that header and add the Apache header below it. For more information, see [CHARTER.md](./CHARTER.md), section 7.a.

### Code style

Match the project's Java style in [docs/code-style.xml](./docs/code-style.xml). You MAY install `./checkstyle/checkstyle-pre-commit` as a Git `pre-commit` hook.

## Open a pull request

### Fill in the template

Every pull request MUST use the template in [`.github/pull_request_template.md`](./.github/pull_request_template.md). Fill in every section. Don't leave placeholder text such as `<!-- describe the motivation -->` or `…`.

Don't open a work-in-progress pull request. Open the pull request when the change is ready to review.

Use a title that states the scope of the change. Keep the section headings from the template as they are. When you add a heading of your own, use sentence case.

### Complete every section

| Section | What to write |
| ------- | ------------- |
| **Summary: Why** | The problem or motivation. |
| **Summary: What** | The change this pull request introduces. |
| **Expected Behavior** | What happens after the change. |
| **Actual Behavior** | What happened before (bug fix) or the new capability (feature). |
| **Steps to Reproduce** | Concrete reproduction steps for a bug fix. |
| **Known Workarounds** | Workarounds if they exist. Otherwise write `None` or remove the section. |
| **Additional Evidence** | Logs, screenshots, or environment details that help a reviewer. |
| **Categorization** | Every applicable checkbox. |

### Link to an existing issue

Replace the issue placeholder:

```text
This PR resolves #<Replace-Me-With-The-Issue-Number-Addressed-By-This-PR> if any.
```

with the issue number, for example:

```text
This PR resolves #42.
```

If no issue exists, create one and link it. Don't leave the placeholder in the description.

### Write the description

Write the body so a reviewer who didn't author the change can follow it:

- Include the motivation and enough context to review the diff.
- Reference related issues, prior discussion, or documentation when that context helps.
- Don't paste large blocks of code into the description. Wrap long logs or stack traces in `<details>` tags.
- State why the change exists, not only which files moved. Use present tense for the new behavior.
- Put verification under a sentence-case heading such as `Test plan`.
- Don't pad the body with review requests or a recap of the diff.

## Engineering rules

You MUST follow these rules when you write docs, pull request titles and bodies, commit messages, and comments.

Don't rewrite existing comments or docs only to match this section.

### Tone

Write like a knowledgeable teammate: conversational and direct, not stiff and not cute. Give the reader the fact they came for. Follow these tone rules:

- MUST use active voice. Name the actor. "The executor applies the proposal." not "The proposal is applied."
- MUST use present tense for current behavior. "The server sends an acknowledgment." not "The server will send an acknowledgment." Use `will` only when the action is later.
- MUST NOT use `please` in instructions or pull request bodies. MUST NOT call a change `simple`, `easy`, or `just`. Those words hide the real cost.
- SHOULD use common two-word contractions (`don't`, `isn't`, `can't`, `you're`). Negation contractions are easier to see while scanning than a lone `not`.
- SHOULD address the reader as `you` in docs and instructions. Use the imperative when you tell them to do something ("Run the test."). In code comments, describe what the system does in third person ("The analyzer skips the excluded topic.").
- SHOULD NOT use first-person plural for the code (`we then update the proposal`). `we` is fine only when it means the Cruise Control project.
- MUST NOT use slang, memes, exclamation points, or `tl;dr`.
- SHOULD put the condition or goal before the instruction, so the reader can skip it: "If the cluster has dead brokers, repair the cluster first." not "Repair the cluster first if it has dead brokers."

### Grammar and punctuation

Follow these grammar and punctuation rules:

- MUST use American spelling (`canceled`, not `cancelled`).
- MUST use the serial comma: "brokers, topics, and partitions."
- SHOULD write short sentences and one idea per sentence when the alternative is a pile of clauses. If a second thought is spliced into the middle with em dashes or parentheses, give it its own sentence, or introduce it with a colon. An em dash is fine for a brief restatement that is not a new claim. Parentheses are fine for a bare label that has no thought of its own, such as an abbreviation gloss or a citation.
- SHOULD make pronoun antecedents obvious. Prefer "this value" over "this." Use singular `they`. Use `that` for restrictive clauses and `which` (with a comma) for nonrestrictive ones.
- MUST use a colon, not a dash, in a list of term/description pairs: `Term: description.` not `Term - description.`
- MUST NOT use `e.g.` or `i.e.`; write `for example` or `that is`.
- SHOULD NOT end a list with `etc.`; introduce the list as incomplete (`such as`) or name the items.
- MUST use one space between sentences.
- MUST NOT put a period on a heading.

### Formatting

Follow these formatting rules:

- MUST use sentence case for headings you add: "Test plan" not "Test Plan." Keep the headings already in [`.github/pull_request_template.md`](./.github/pull_request_template.md). Task headings start with a bare infinitive ("Add the metric"). Concept headings are noun phrases ("Retention metrics"). Avoid starting a heading with an `-ing` verb.
- MUST put identifiers, filenames, flags, class names, methods, config keys, status codes, and other code in backticks. Don't inflect a code token; add an English noun and inflect that: "`Goal` objects" not "`Goals`."
- MUST use descriptive link text. Write "For more information, see [CONTRIBUTING.md](./CONTRIBUTING.md)." not "click here" or "see this document."
- SHOULD number a list only when order matters. Use bullets otherwise. Introduce a list with a complete sentence, usually ending in a colon. Capitalize each item. End the item with a period if it contains a verb; skip the period for a single word, a code-only item, or a document title.
- SHOULD spell out an uncommon abbreviation on first use (`Technical Steering Committee (TSC)`), then use the abbreviation. Don't spell out `API`, `HTTP`, `URL`, `SQL`, or file formats the audience already knows. Don't use an acronym as a verb ("Use SSH to log in," not "ssh into").
- MUST NOT use `&` as a substitute for `and`.

### Word choices

Prefer the precise word. If a term is established Cruise Control or Kafka vocabulary (`replica`, `ISR`, `leader`, `follower`, `log`, `offset`, `goal`, `proposal`, `broker`), keep it.

| Avoid | Prefer |
| ----- | ------ |
| `allows you to` | `lets you` |
| `utilize`, `leverage` | `use` |
| `currently`, `at this time`, `as of this writing` | omit; state the fact |
| `will` for current behavior | present tense |
| `please note` | the note, with no preamble |
| `whitelist` / `blacklist` | `allowlist` / `denylist`, or the action ("deny requests from") |
| `dummy` (for a stand-in value) | `placeholder` |
| `above` / `below` for a position in a doc | `preceding` / `following` |
| `abort` in general prose about stopping work | `stop`, `cancel`, or `end` |
| `sanity check` | `check` |

Keep a term from the Avoid column when it names an existing identifier rather than describing one. Write that name in backticks, and use the preferred word only in prose that is not naming it.

### Comments

Write self-documenting code. Comments SHOULD be rare and explain **why** the code is this way, not **what** it does. Typical reasons are a non-obvious constraint, a workaround, or a deliberate deviation. Match the surrounding comment density and never restate the code.

When you write a comment or a Javadoc sentence:

- Use present tense and active voice.
- Don't start with "This class" or "This method."
- For a method that returns something, start with a verb: `Returns`, `Gets`, `Checks whether`, `Sets`, `Deletes`.
- Boolean returns: `True if ...; false otherwise.`
- Keep the first sentence able to stand alone; some generators take only that sentence as the summary.

## Commits

### Message

Write an imperative, concise subject that states why the change exists, not only what files moved. Match the repository's existing subject style, for example `Migrate Executor off private Kafka APIs` or `Ensure tests wait for metadata propagation`.

Don't invent a commit-title convention the surrounding history doesn't use.

Use present tense for the new behavior in the body. Put testing notes in the body when they aren't obvious from the subject.

### Developer Certificate of Origin

Every commit MUST be signed off with the Developer Certificate of Origin, as required by [CONTRIBUTING.md](./CONTRIBUTING.md) and [CHARTER.md](./CHARTER.md) section 7.b.ii:

```text
git commit -s -m "Your commit message"
```

The `Signed-off-by` name and email MUST match the Git `user.name` and `user.email` of the human who is responsible for the change.

### AI attribution

Follow the generative AI policy in [CONTRIBUTING.md](./CONTRIBUTING.md).

If an AI tool prepared any part of a commit, the commit MUST name the tool and its version in a `Co-Authored-By`, `Generated-by`, `Assisted-by`, or similar trailer:

```text
Co-Authored-By: <AI tool name and version>
```

Example:

```text
Ensure tests wait for metadata propagation

Executor tests fail when the broker has not yet propagated topic
metadata after topic creation.

Co-Authored-By: Cursor Grok 4.6
Signed-off-by: Jane Developer <jane@example.com>
```

Don't attribute a commit to an AI tool when a human wrote the change without one.
