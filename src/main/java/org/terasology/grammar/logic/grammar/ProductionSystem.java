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
package org.terasology.grammar.logic.grammar;

import org.terasology.grammar.logic.grammar.shapes.Shape;
import org.terasology.grammar.logic.grammar.shapes.ShapeSymbol;

import java.util.List;
import java.util.Map;

/**
 * @author Tobias 'Skaldarnar' Nett
 * @version 0.3 Date: 26.08.12
 *          <p/>
 *          A ProductionSystem consists of a mapping from ShapeSymbols to a list of successor shapes and an initial axiom. The initial axiom
 *          defines where to start a derivation process.
 */
public class ProductionSystem {
    /** Map with rule mappings from names to shape. */
    private Map<String, List<Shape>> rules;
    /** The initial axiom to start with. */
    private ShapeSymbol initialAxiom;

    public ProductionSystem(Map<String, List<Shape>> rules, ShapeSymbol initialAxiom) {
        // JAVA7 : Object.requireNonNull(…);
        if (rules == null || initialAxiom == null) {
            throw new IllegalArgumentException("no null params allowed.");
        }
        this.rules = rules;
        this.initialAxiom = initialAxiom;
    }

    public ShapeSymbol getInitialAxiom() {
        return initialAxiom;
    }

    public Map<String, List<Shape>> getRules() {
        return rules;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String key : rules.keySet()) {
            List<Shape> l = rules.get(key);
            builder.append(key).append(" ::- ");
            builder.append(l.get(0).toString());
            for (int i = 1; i < l.size(); i++) {
                builder.append(" | ").append(l.get(i).toString());
            }
            builder.append("\n");
        }
        builder.append(";");
        return builder.toString();
    }
}
