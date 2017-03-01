package mslinks;

/*-
 * #%L
 * FOKProjects MSLinks
 * %%
 * Copyright (C) 2016 - 2017 Frederik Kammel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.junit.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class ShellLinkTest {

    private Path folder = null;
    private Path file = null;
    private Path shortcut = null;

    @Before
    public void setUpPaths() throws IOException {
        folder = Files.createTempDirectory("mslinks_test");
        file = Files.createFile(folder.resolve("pause.bat"));
        shortcut = folder.resolve("testlink.lnk");
    }

    @After
    public void tidyUp() throws IOException {
        Files.deleteIfExists(shortcut);
        Files.deleteIfExists(file);
        Files.deleteIfExists(folder);
    }

    @Test
    public void mainTest() throws IOException {
        // Test is trivial. Copied from original main() example.
        try {
            assertFalse(Files.exists(shortcut, LinkOption.NOFOLLOW_LINKS));

            ShellLink sl = ShellLink.createLink(file.toString())
                    .setWorkingDir("..")
                    .setIconLocation("%SystemRoot%\\system32\\SHELL32.dll");
            sl.getHeader().setIconIndex(128);
            sl.getConsoleData()
                    .setFont(mslinks.extra.ConsoleData.Font.Consolas)
                    .setFontSize(24)
                    .setTextColor(5);

            sl.saveTo(shortcut.toString());

            assertTrue(Files.exists(shortcut, LinkOption.NOFOLLOW_LINKS));
            assertTrue(Files.exists(file));
            assertTrue(Files.exists(shortcut));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void pathTest() throws IOException {
        // Test is trivial. Modified from original main() example for new Path interface.
        try {
            assertFalse(Files.exists(shortcut, LinkOption.NOFOLLOW_LINKS));

            ShellLink sl = ShellLink.createLink(file)
                    .setWorkingDir("..")
                    .setIconLocation("%SystemRoot%\\system32\\SHELL32.dll");
            sl.getHeader().setIconIndex(128);
            sl.getConsoleData()
                    .setFont(mslinks.extra.ConsoleData.Font.Consolas)
                    .setFontSize(24)
                    .setTextColor(5);

            sl.saveTo(shortcut);

            assertTrue(Files.exists(shortcut, LinkOption.NOFOLLOW_LINKS));
            assertTrue(Files.exists(file));
            assertTrue(Files.exists(shortcut));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void createLinkTest() {
        try {
            assertFalse(Files.exists(shortcut, LinkOption.NOFOLLOW_LINKS));

            ShellLink sl = ShellLink.createLink(file.toString(), shortcut.toString());

            assertTrue(Files.exists(shortcut, LinkOption.NOFOLLOW_LINKS));
            assertTrue(Files.exists(file));
            assertTrue(Files.exists(shortcut));
        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void createLinkPathTest() {
        try {
            assertFalse(Files.exists(shortcut, LinkOption.NOFOLLOW_LINKS));

            ShellLink sl = ShellLink.createLink(file, shortcut);

            assertTrue(Files.exists(shortcut, LinkOption.NOFOLLOW_LINKS));
            assertTrue(Files.exists(file));
            assertTrue(Files.exists(shortcut));
        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

}
