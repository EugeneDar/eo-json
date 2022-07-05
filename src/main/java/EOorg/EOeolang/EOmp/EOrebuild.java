/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 Eugene Darashkevich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package EOorg.EOeolang.EOmp;
import org.eolang.*;

import java.util.ArrayList;

@SuppressWarnings("PMD.AvoidDollarSigns")
public class EOrebuild extends PhDefault {

    @SuppressWarnings("PMD.ConstructorOnlyInitializesOrCallOtherConstructors")
    public EOrebuild(final Phi sigma) {
        super(sigma);
        this.add("harr", new AtFree());
        this.add("arr", new AtFree());
        this.add("φ", new AtComposite(this, rho -> {
            final Phi[] harr = new Dataized(rho.attr("harr").get()).take(Phi[].class);
            final Phi[] arr = new Dataized(rho.attr("arr").get()).take(Phi[].class);

            ArrayList<Integer> hashes = new ArrayList<>();

            for (final Phi item : harr) {
                final Long x = new Dataized(item).take(Long.class);
                hashes.add(Math.toIntExact(x));
            }

            // This value can be changed for memory or speed optimization
            int TABLE_SIZE = hashes.size();

            ArrayList<ArrayList<Phi>> table = new ArrayList<>();

            for (int i = 0;i < TABLE_SIZE;++i) {
                table.add(new ArrayList<>());
            }

            for (int i = 0;i < arr.length;++i) {
                table.get(hashes.get(i) % TABLE_SIZE).add(arr[i]);
            }

            Phi[] result = new Phi[TABLE_SIZE];

            for (int i = 0;i < TABLE_SIZE;++i) {
                Phi[] array = new Phi[table.get(i).size()];
                for (int j = 0;j < table.get(i).size();++j) {
                    array[j] = table.get(i).get(j);
                }
                result[i] = new Data.ToPhi(array);
            }

            return new Data.ToPhi(result);
        }));
    }

}