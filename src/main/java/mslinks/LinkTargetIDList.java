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
import mslinks.data.ItemID;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class LinkTargetIDList extends LinkedList<ItemID> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3619823553066465595L;

    public LinkTargetIDList() {
    }

    public LinkTargetIDList(ByteReader data) throws IOException, ShellLinkException {
        int size = (int) data.read2bytes();

        int pos = data.getPosition();

        boolean binary = false;
        int s = (int) data.read2bytes();
        while (s != 0) {
            s -= 2;
            if (binary) {
                byte[] b = new byte[s];
                for (int i = 0; i < s; i++)
                    b[i] = (byte) data.read();
                add(new ItemID(b));
            } else try {
                add(new ItemID(data, s));
            } catch (UnsupportedCLSIDException e) {
                System.err.println("unsupported CLSID");
                binary = true;
            }
            s = (int) data.read2bytes();
        }

        pos = data.getPosition() - pos;
        if (pos != size)
            throw new ShellLinkException();
    }

    public void serialize(ByteWriter bw) throws IOException {
        int size = 2;
        byte[][] b = new byte[size()][];
        int i = 0;
        for (ItemID j : this) {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ByteWriter w = new ByteWriter(ba);

            j.serialize(w);
            b[i++] = ba.toByteArray();
        }
        for (byte[] j : b)
            size += j.length + 2;

        bw.write2bytes(size);
        for (byte[] j : b) {
            bw.write2bytes(j.length + 2);
            bw.writeBytes(j);
        }
        bw.write2bytes(0);
    }

    public boolean isCorrect() {
        for (ItemID i : this)
            if (i.getType() == ItemID.TYPE_UNKNOWN)
                return false;
        return true;
    }
}
