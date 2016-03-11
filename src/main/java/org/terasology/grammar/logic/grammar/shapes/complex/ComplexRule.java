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

import java.util.List;

/** @author Tobias 'skaldarnar' Nett */
public abstract class ComplexRule extends Shape {

    /**
     * Returns the successor elements of this complex shape.
     * <p/>
     * The successor elements of a complex shape are for example the arguments of a _divide_ or _repeat_ rule. The successor elements cannot
     * be simply looked up in the org.terasology.logic.grammar rules but rather depend on the specific complex shape rule/command they
     * belong to.
     *
     * @return the successor elements of the complex shape rule
     */
    public abstract List<Shape> getElements();
}
