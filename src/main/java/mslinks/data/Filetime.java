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
package mslinks.data;

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

import java.io.IOException;
import java.util.GregorianCalendar;

@SuppressWarnings("WeakerAccess")
public class Filetime extends GregorianCalendar implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2258831109960118017L;
    private long residue;

    public Filetime() {
        super();
    }

    public Filetime(ByteReader data) throws IOException {
        this(data.read8bytes());
    }

    public Filetime(long time) {
        long t = time / 10000;
        residue = time - t;
        setTimeInMillis(t);
        add(GregorianCalendar.YEAR, -369);
    }

    public long toLong() {
        GregorianCalendar tmp = (GregorianCalendar) clone();
        tmp.add(GregorianCalendar.YEAR, 369);
        return tmp.getTimeInMillis() + residue;
    }

    public void serialize(ByteWriter bw) throws IOException {
        bw.write8bytes(toLong());
    }

    public String toString() {
        return String.format("%d:%d:%d %d.%d.%d",
                get(GregorianCalendar.HOUR_OF_DAY), get(GregorianCalendar.MINUTE), get(GregorianCalendar.SECOND),
                get(GregorianCalendar.DAY_OF_MONTH), get(GregorianCalendar.MONTH) + 1, get(GregorianCalendar.YEAR));
    }
}
