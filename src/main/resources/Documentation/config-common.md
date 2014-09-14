Common configuration for `its-base`-based plugins
=================================================

#### Table of Contents
* [Identifying ITS ids][identifying-its-ids]
* [Enabling ITS integration][enabling-its-integration]
* [Configuring rules of when to take which actions in the ITS][configure-rules]
* [Legacy configuration][legacy-configuration]



[identifying-its-ids]: #identifying-its-ids
<a name="identifying-its-ids">Identifying ITS ids</a>
-----------------------------------------------------

In order to extract ITS ids from commit messages, @PLUGIN@ uses
[commentlink][upstream-comment-link-doc]s of name "`@PLUGIN@`".

The first group of `commentlink.@PLUGIN@.match` is considered the
issue id.

So for example having

```
[commentLink "@PLUGIN@"]
    match = [Bb][Uu][Gg][ ]*([1-9][0-9]*)
    html = "<a href=\"http://my.issure.tracker.example.org/show_bug.cgi?id=$1\">(bug $1)</a>"
    association = SUGGESTED
```

in `etc/gerrit.config` would allow to match the issues `4711`, `167`
from a commit message like

```
Sample commit message relating to bug 4711, and bug 167.
```

By setting a `commentlink`'s `association` (see above's example), it
is possible to require commits to carry ITS references; the following
values are supported (default is `OPTIONAL`):

MANDATORY
:	 One or more issue-ids are required in the git commit message, otherwise
	 the git push will be rejected.

SUGGESTED
:	 Whenever the git commit message does not contain one or more issue-ids,
	 a warning message is displayed as a suggestion on the client.

OPTIONAL
:	 Bug-ids are liked when found in the git commit message, no warning is
	 displayed otherwise.



[enabling-its-integration]: #enabling-its-integration
<a name="enabling-its-integration">Enabling ITS integration</a>
---------------------------------------------------------------

It can be configured per project whether the issue tracker
integration is enabled or not. To enable the issue tracker integration
for a project the project must have the following entry in its
`project.config` file in the `refs/meta/config` branch:

```
  [plugin "@PLUGIN@"]
    enabled = true
```

If `plugin.@PLUGIN@.enabled` is not specified in the `project.config`
file the value is inherited from the parent project. If it is not
set on any parent project the issue integration is disabled for this
project.

By setting `plugin.@PLUGIN@.enabled` to true in the `project.config`
of the `All-Projects` project the issue tracker integration can be
enabled by default for all projects. During the initialization of the
plugin you are asked if the issue integration should be enabled by
default for all projects and if yes this setting in the
`project.config` of the `All-Projects` project is done automatically.

With this it is possible to support integration with multiple
issue tracker systems on a server. E.g. a project can choose if it
wants to enable integration with Jira or with Bugzilla.

If child projects must not be allowed to disable the issue tracker
system integration a project can enforce the issue tracker system
integration for all child projects by setting
`plugin.@PLUGIN@.enabled` to `enforced`.

The issue tracker system integration can be limited to specific
branches by setting `plugin.@PLUGIN@.branch`. The branches may be
configured using explicit branch names, ref patterns, or regular
expressions. Multiple branches may be specified.

E.g. to limit the issue tracker system integration to the `master`
branch and all stable branches the following could be configured:

```
  [plugin "@PLUGIN@"]
    enabled = true
    branch = refs/heads/master
    branch = ^refs/heads/stable-.*
```



[configure-rules]: #configure-rules
<a name="configure-rules">Configuring rules of when to take which actions in the ITS</a>
----------------------------------------------------------------------------------------

Setting up which event in Gerrit (E.g.: “Change Merged”, or “User
‘John Doe’ voted ‘+2’ for ‘Code-Review’ on a change”) should trigger
which action on the ITS (e.g.: “Set issue's status to ‘Resolved’”) is
configured through a [rule base][rule-base] in
`etc/its/action.config`.

[rule-base]: config-rulebase-common.html



[legacy-configuration]: #legacy-configuration
<a name="legacy-configuration">Legacy configuration</a>
-------------------------------------------------------

In this section we present the legacy configuration that uses
`etc/gerrit.config` directly. As this legacy part will be removed at
some point, please upgrade to the rule [rule based
approach][rule-base].

The following configuration settings are available:

`@PLUGIN@.commentOnChangeAbandoned`
:	If true, abandoning a change adds an ITS comment to the change's
	associated issue.

	Default is `true`.

`@PLUGIN@.commentOnChangeCreated`
:	If true, creating a change adds an ITS comment to the change's
	associated issue.

	Default is `false`.

`@PLUGIN@.commentOnChangeMerged`
:	If true, merging a change's patch set adds an ITS comment to
	the change's associated issue.

	Default is `true`.

`@PLUGIN@.commentOnChangeRestored`
:	If true, restoring an abandoned change adds an ITS comment to
	the change's associated issue.

	Default is `true`.

`@PLUGIN@.commentOnCommentAdded`
:	If true, adding a comment and/or review to a change in Gerrit
	adds an ITS comment to the change's associated issue.

	Default is `true`.

`@PLUGIN@.commentOnFirstLinkedPatchSetCreated`
:	If true, creating a patch set for a change adds an ITS comment
	to the change's associated issue, if the issue has not been
	mentioned in previous patch sets of the same change.

	Default is `false`.

`@PLUGIN@.commentOnPatchSetCreated`
:	If true, creating a patch set for a change adds an ITS comment
	to the change's associated issue.

	Default is `true`.

`@PLUGIN@.commentOnRefUpdatedGitWeb`
:	If true, updating a ref adds a GitWeb link to the associated
	issue.

	Default is `true`.

[Back to @PLUGIN@ documentation index][index]

[index]: index.html