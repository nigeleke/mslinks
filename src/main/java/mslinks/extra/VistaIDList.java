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
package mslinks.extra;

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
import mslinks.Serializable;
import mslinks.ShellLinkException;

import java.io.IOException;
import java.util.LinkedList;

public class VistaIDList implements Serializable {

    public static final int signature = 0xA000000C;

    private final LinkedList<byte[]> list = new LinkedList<>();

    public VistaIDList(ByteReader br, int size) throws ShellLinkException, IOException {
        if (size < 0xa)
            throw new ShellLinkException();

        int s = (int) br.read2bytes();
        while (s != 0) {
            s -= 2;
            byte[] b = new byte[s];
            for (int i = 0; i < s; i++)
                b[i] = (byte) br.read();
            list.add(b);
            s = (int) br.read2bytes();
        }
    }

    @Override
    public void serialize(ByteWriter bw) throws IOException {
        int size = 10;
        for (byte[] i : list)
            size += i.length + 2;
        bw.write2bytes(size);
        for (byte[] i : list) {
            bw.write2bytes(i.length + 2);
            for (byte j : i)
                bw.write(j);
        }
        bw.write2bytes(0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (byte[] b : list)
            sb.append(new String(b)).append("\n");
        return sb.toString();
    }
}
