package unalcol.random.util;

import unalcol.random.integer.*;

//
// Unalcol Random generation Pack 1.0 by Jonatan Gomez-Perdomo
// http://disi.unal.edu.co/profesores/jgomezpe/unalcol/random/
//
/**
 * <p>A random generator of predefined objects</p>
 *
 * <P>
 *
 * <P>
 * <A HREF="http://disi.unal.edu.co/profesores/jgomezpe/source/unalcol/random/util/RandObj.java">
 * Source code </A> is available.
 * <P>
 *
 * <h3>License</h3>
 *
 * Copyright (c) 2014 by Jonatan Gomez-Perdomo. <br>
 * All rights reserved. <br>
 *
 * <p>Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <ul>
 * <li> Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * <li> Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <li> Neither the name of the copyright owners, their employers, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * </ul>
 * <p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 *
 *
 * @author <A HREF="http://disi.unal.edu.co/profesores/jgomezpe"> Jonatan Gomez-Perdomo </A>
 * (E-mail: <A HREF="mailto:jgomezpe@unal.edu.co">jgomezpe@unal.edu.co</A> )
 * @version 1.0
 * @param <T> Type of the set of objects that can be randomly selected
 */
public class RandObj<T> {
    /**
     * Set of predefined objects that can be randomly selected
     */
    protected T[] objects;
    /**
     * Objects density function
     */
    protected RandInt g;

    /**
     * Created a random generator of predefined objects
     * @param objects Set of predefined objects that can be randomly generated
     */
    public RandObj(T[] objects) {
        this.objects = objects;
        g = new IntUniform(this.objects.length);
    }

    /**
     * Created a random generator of predefined objects
     * @param objects Set of predefined objects that can be randomly generated
     * @param g Objects density function
     */
    public RandObj(T[] objects, RandInt g) {
        this.objects = objects;
        this.g = g;
    }

    /**
     * Generates a predefined object following the associated objects distribution
     * @return A predefined object following the associated objects distribution
     */
    public T next() {
        return objects[g.next()];
    }

    /**
     * Returns a set of random objects
     * @param v Array where objects will be stored
     * @param m The total number of random objects to be generated
     */
    public void raw(T[] v, int m) {
        int[] index = g.generate(m);
        for (int i = 0; i < m; i++) {
            v[i] = objects[index[i]];
        }
    }

    /**
     * Returns a set of random objects
     * @param m The total number of random objects to be generated
     * @return A set of m random objects
     */
    @SuppressWarnings("unchecked")
	public T[] raw(int m) {
        T[] v = null;
        if (m > 0) {
            v = (T[])new Object[m];
            raw(v, m);
        }
        return v;
    }
}