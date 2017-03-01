/*
	https://github.com/BlackOverlord666/mslinks
	
	Copyright (c) 2015 Dmitrii Shamrikov

	Licensed under the WTFPL
	You may obtain a copy of the License at
 
	http://www.wtfpl.net/about/
 
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
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


import io.ByteReader;
import io.ByteWriter;
import mslinks.data.*;

import java.io.IOException;

@SuppressWarnings({"SameParameterValue", "UnusedReturnValue", "WeakerAccess"})
public class ShellLinkHeader implements Serializable {
    public static final int SW_SHOWNORMAL = 1;
    public static final int SW_SHOWMAXIMIZED = 3;
    public static final int SW_SHOWMINNOACTIVE = 7;
    private static final int headerSize = 0x0000004C;
    private static final GUID clsid = new GUID("00021401-0000-0000-C000-000000000046");
    private LinkFlags lf;
    private FileAttributesFlags faf;
    private Filetime creationTime, accessTime, writeTime;
    private int fileSize, iconIndex, showCommand;
    private HotKeyFlags hkf;

    public ShellLinkHeader() {
        lf = new LinkFlags(0);
        faf = new FileAttributesFlags(0);
        creationTime = new Filetime();
        accessTime = new Filetime();
        writeTime = new Filetime();
        showCommand = SW_SHOWNORMAL;
        hkf = new HotKeyFlags();
    }

    public ShellLinkHeader(ByteReader data) throws ShellLinkException, IOException {
        int size = (int) data.read4bytes();
        if (size != headerSize)
            throw new ShellLinkException();
        GUID g = new GUID(data);
        if (!g.equals(clsid))
            throw new ShellLinkException();
        lf = new LinkFlags(data);
        faf = new FileAttributesFlags(data);
        creationTime = new Filetime(data);
        accessTime = new Filetime(data);
        writeTime = new Filetime(data);
        fileSize = (int) data.read4bytes();
        iconIndex = (int) data.read4bytes();
        showCommand = (int) data.read4bytes();
        if (showCommand != SW_SHOWNORMAL && showCommand != SW_SHOWMAXIMIZED && showCommand != SW_SHOWMINNOACTIVE)
            throw new ShellLinkException();
        hkf = new HotKeyFlags(data);
        data.read2bytes();
        data.read8bytes();
    }

    public LinkFlags getLinkFlags() {
        return lf;
    }

    public FileAttributesFlags getFileAttributesFlags() {
        return faf;
    }

    public Filetime getCreationTime() {
        return creationTime;
    }

    public Filetime getAccessTime() {
        return accessTime;
    }

    public Filetime getWriteTime() {
        return writeTime;
    }

    public HotKeyFlags getHotKeyFlags() {
        return hkf;
    }

    public int getFileSize() {
        return fileSize;
    }

    public ShellLinkHeader setFileSize(long n) {
        fileSize = (int) n;
        return this;
    }

    public int getIconIndex() {
        return iconIndex;
    }

    public ShellLinkHeader setIconIndex(int n) {
        iconIndex = n;
        return this;
    }

    public int getShowCommand() {
        return showCommand;
    }

    public ShellLinkHeader setShowCommand(int n) throws ShellLinkException {
        if (n == SW_SHOWNORMAL || n == SW_SHOWMAXIMIZED || n == SW_SHOWMINNOACTIVE) {
            showCommand = n;
            return this;
        } else
            throw new ShellLinkException();
    }

    public void serialize(ByteWriter bw) throws IOException {
        bw.write4bytes(headerSize);
        clsid.serialize(bw);
        lf.serialize(bw);
        faf.serialize(bw);
        creationTime.serialize(bw);
        accessTime.serialize(bw);
        writeTime.serialize(bw);
        bw.write4bytes(fileSize);
        bw.write4bytes(iconIndex);
        bw.write4bytes(showCommand);
        hkf.serialize(bw);
        bw.write2bytes(0);
        bw.write8bytes(0);
    }
}
