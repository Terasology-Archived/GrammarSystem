/*
 * Copyright 2016 MovingBlocks
 *
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
 */
package org.terasology.grammar.logic.grammar.shapes;

import org.terasology.math.TeraMath;
import org.terasology.math.geom.Matrix4i;
import org.terasology.math.geom.Vector3i;

/**
 * A shape symbol describes a org.terasology.logic.grammar element that can be derived directly via a org.terasology.logic.grammar rule.
 * <p/>
 * It has a (unique) label - typically the symbol you use in the org.terasology.logic.grammar. There is no further information a {@code
 * ShapeSymbol} holds except for the geometric information of a Shape itself.
 *
 * @author Tobias 'skaldarnar' Nett
 */
public class ShapeSymbol extends Shape {

    /** The string representation of the shape symbols label. */
    private String label = "";

    /**
     * A new ShapeSymbol is created with the specified label. The default probability of 1.0 is used for this symbol.
     *
     * @param label the symbol's label - not null
     */
    public ShapeSymbol(String label) {
        // JAVA7 : Objects.requireNonNull(label);
        if (label == null) {
            throw new IllegalArgumentException("ShapeSymbol cannot be null.");
        }
        this.label = label;
    }

    /**
     * A new ShapeSymbol is created with the specified label and the given probability. </p> If the specified probability is less then zero
     * or greater then one, the probability value will be clamped into the bounds between 0 and 1. Thus, no exception will occur.
     *
     * @param label       the label of the shape symbol
     * @param probability the specific probability in the range from 0 to 1
     */
    public ShapeSymbol(String label, float probability) {
        this(label);
        this.probability = TeraMath.clamp(probability, 0f, 1f);
    }

    /**
     * The shape symbols string representation is equal to its label.
     *
     * @return the string representation of the shape's label.
     */
    @Override
    public String toString() {
        return label + "\n" + matrix + "\n" + dimension;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Shape clone() {
        ShapeSymbol clone = new ShapeSymbol(label, probability);
        clone.setActive(active);
        clone.setMatrix(new Matrix4i(matrix));
        clone.setDimension(new Vector3i(dimension));
        return clone;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShapeSymbol)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        ShapeSymbol that = (ShapeSymbol) o;

        return label.equals(that.label);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + label.hashCode();
        return result;
    }
}

