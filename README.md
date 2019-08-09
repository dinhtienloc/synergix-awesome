# synergix-intellij-plugin
This plugin provides a bunch of utilities to help developers code faster with Synergix Framework.

Plugin repo: https://plugins.jetbrains.com/plugin/11303-synergix-awesome

### Features
* Code completion: SynComponent's attributes such as <i>labelKey</i>, <i>numberType</i>, <i>data-type</i>...
* File Template: XML Entity File.
* Live Templates: auto generating code for common columns in XML Entity File.
* String literal navigator everywhere.
* Sync DB Run Configuration: allow run Sync DB directly from IDE.
* Synergix Screens Tool Window: manage and display the code structure (XHTML + Bean + Service) for screens created by Synergix Framework.

### Change log
**2.0**
* New feature: Synergix Screens Tool Window

**1.3**
* Fix Sync DB Configuration is not saved after closing IDE.
* Move the IDE compatibility version to 162.
* Refactor the code.

**1.2**
* Include all properties from settings.ini to Sync DB Configuration.
* Fix Sync DB Configuration is not saved after closing IDE.

**1.1**
* Fix Sync DB don't stop when extracting tables get errors.
* Using internal Sync DB setting properties (editable) instead of external settings.ini file.
* More compatible with older idea version.

**1.0**
* Code completion for some SynComponent's attributes: <b>labelKey</b>, <b>numberType</b>, <b>data-type</b>...
* Support create XML Entity File.
* Live Templates: auto generating code for common columns in XML Entity File.
* String literal navigator everywhere.
* Create Sync DB Run Configuration, allow run Sync DB command line from IDE.