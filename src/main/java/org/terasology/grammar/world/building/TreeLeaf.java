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
package org.terasology.grammar.world.building;

import org.terasology.grammar.logic.grammar.shapes.Shape;
import org.terasology.grammar.logic.grammar.shapes.TerminalShape;
import org.terasology.grammar.world.block.BlockCollection;

import java.util.List;

/**
 * @author Tobias 'Skaldarnar' Nett
 *         <p/>
 *         A tree leaf contains only terminal shapes in the derivation tree.
 */
public class TreeLeaf extends Tree {

    private TerminalShape terminal;
    private Tree parent;

    public TreeLeaf(TerminalShape terminal) {
        BlockCollection c = new BlockCollection();
        this.terminal = terminal;
    }

    @Override
    public BlockCollection derive() {
        return terminal.getValue();
    }

    @Override
    public void setParent(Tree parent) {
        this.parent = parent;
    }

    @Override
    public Shape getShape() {
        return terminal;
    }

    @Override
    public List<TreeNode> findActiveNodes() {
        return null;
    }

    public TerminalShape getTerminal() {
        return terminal;
    }

    public Tree getParent() {
        return parent;
    }
}
