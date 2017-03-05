mslinks
=======
Library for parsing and creating Windows shortcut files (.lnk)
***
This is a fork of [vatbub/mslinks](https://github.com/vatbub/mslinks), followed by a merge [diffplug/mslinks (no longer active)](https://githib.com/diffplug/mlslinks), both
of which were forked from [BlackOverlord666/mslinks](https://github.com/BlackOverlord666/mslinks).

AFAIK the most recent maven repository publication is Frederik's at:
```xml
<dependency>
	<groupId>com.github.vatbub</groupId>
	<artifactId>mslinks</artifactId>
	<version>1.0.3.1</version>
</dependency>
```

**PLEASE NOTE** as with Frederick, this fork is not under active developement. The repository at [BlackOverlord666/mslinks](https://github.com/BlackOverlord666/mslinks) is still seen as the master.

My amendments were to provide:

1. <code>Path</code> alternatives to the <code>String</code> interfaces.

1. Add simple tests (based on the original <code>main()</code> example)

***

Partial implementation of [Shell Link (.LNK) Binary File Format](http://msdn.microsoft.com/en-us/library/dd871305.aspx)

You can edit most properties of the link such as working directory, tooltip text, icon, command line arguments, hotkeys, create links to LAN shared files and directories but followed features are not implemented:

* extra data blocks: Darwin, IconEnvironment, KnownFolder, PropertyStore, Shim, SpecialFolder
* most options in LinkTargetIDList because it not documented, only key parts for resolving links are implemented, others are zero stub
* you can use environment variables in target path but they are resolved at creation time and not stored in the lnk file

Easiest way to create link with default parameters: `ShellLink.createLink("targetfile", "linkfile.lnk")`

Next sample demonstrates creating link for .bat file with setting working directory, icon and tune font parameters for console
```java
package mslinks;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		ShellLink sl = ShellLink.createLink("pause.bat")
			.setWorkingDir("..")
			.setIconLocation("%SystemRoot%\\system32\\SHELL32.dll");
		sl.getHeader().setIconIndex(128);
		sl.getConsoleData()
			.setFont(mslinks.extra.ConsoleData.Font.Consolas)
			.setFontSize(24)
			.setTextColor(5);
				
		sl.saveTo("testlink.lnk");
		System.out.println(sl.getWorkingDir());
		System.out.println(sl.resolveTarget());
	}
}

```

Final example creates recursive link that blocks explorer on Windows 7 while trying to get into the containing directory :D
```java
package mslinks;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		ShellLink sl = ShellLink.createLink("test.lnk");
		sl.getHeader().getLinkFlags().setAllowLinkToLink();
		sl.saveTo("test.lnk");
	}
}
```
