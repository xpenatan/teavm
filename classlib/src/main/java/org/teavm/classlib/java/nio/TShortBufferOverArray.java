/*
 *  Copyright 2014 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.classlib.java.nio;

import org.teavm.jso.typedarrays.ArrayBufferView;
import org.teavm.jso.typedarrays.HasArrayBufferView;
import org.teavm.jso.typedarrays.Int16Array;

class TShortBufferOverArray extends TShortBufferImpl implements HasArrayBufferView {
    boolean readOnly;
    int start;
    short[] array;
    Int16Array jsArray;
    private boolean isDirty;

    public TShortBufferOverArray(int capacity) {
        this(0, capacity, new short[capacity], 0, capacity, false);
    }

    public TShortBufferOverArray(int start, int capacity, short[] array, int position, int limit, boolean readOnly) {
        super(capacity, position, limit);
        this.start = start;
        this.readOnly = readOnly;
        this.array = array;
    }

    @Override
    TShortBuffer duplicate(int start, int capacity, int position, int limit, boolean readOnly) {
        return new TShortBufferOverArray(this.start + start, capacity, array, position, limit, readOnly);
    }

    @Override
    short getElement(int index) {
        return array[index + start];
    }

    @Override
    void putElement(int index, short value) {
        isDirty = true;
        array[index + start] = value;
    }

    @Override
    boolean isArrayPresent() {
        return true;
    }

    @Override
    short[] getArray() {
        return array;
    }

    @Override
    int getArrayOffset() {
        return start;
    }

    @Override
    boolean readOnly() {
        return readOnly;
    }

    @Override
    public TByteOrder order() {
        return TByteOrder.BIG_ENDIAN;
    }

    @Override
    public ArrayBufferView getArrayBufferView() {
        if(isDirty) {
            isDirty = false;
            int length = array.length;
            jsArray = new Int16Array(length);
            for(int i = 0; i < length; i++) {
                short value = array[i];
                jsArray.set(i, value);
            }
        }
        return jsArray;
    }
}
