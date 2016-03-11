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
package org.terasology.grammar.logic.grammar.shapes.complex;

import org.terasology.grammar.logic.grammar.shapes.Shape;

/** @author Tobias 'skaldarnar' Nett */
public class SplitArg {
    public enum SplitType {
        WALLS, INNER;
    }

    private SplitType type;
    private Shape symbol;

    public SplitArg(SplitType type, Shape symbol) {
        this.type = type;
        this.symbol = symbol;
    }

    public Shape getShape() {
        return symbol;
    }

    public SplitType getType() {
        return type;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(type.name());
        builder.append("] \t");
        builder.append(symbol.toString());
        return builder.toString();
    }
}
