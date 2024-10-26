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
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.HasArrayBufferView;

class TFloatBufferOverArray extends TFloatBufferImpl implements HasArrayBufferView {
    boolean readOnly;
    int start;
    float[] array;
    Float32Array jsArray;
    private boolean isDirty;

    public TFloatBufferOverArray(int capacity) {
        this(0, capacity, new float[capacity], 0, capacity, false);
    }

    public TFloatBufferOverArray(int start, int capacity, float[] array, int position, int limit, boolean readOnly) {
        super(capacity, position, limit);
        this.start = start;
        this.readOnly = readOnly;
        this.array = array;
    }

    @Override
    TFloatBuffer duplicate(int start, int capacity, int position, int limit, boolean readOnly) {
        return new TFloatBufferOverArray(this.start + start, capacity, array, position, limit, readOnly);
    }

    @Override
    float getElement(int index) {
        return array[index + start];
    }

    @Override
    void putElement(int index, float value) {
        array[index + start] = value;
        isDirty = true;
    }

    @Override
    boolean isArrayPresent() {
        return true;
    }

    @Override
    float[] getArray() {
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
            jsArray = new Float32Array(length);
            for(int i = 0; i < length; i++) {
                float value = array[i];
                jsArray.set(i, value);
            }
        }
        return jsArray;
    }
}
