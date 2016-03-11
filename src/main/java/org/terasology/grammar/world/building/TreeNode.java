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
import org.terasology.grammar.world.block.BlockCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Tobias 'Skaldarnar' Nett
 * @version 0.1
 *          <p/>
 *          A TreeNode consists of a ComplexShape object and zero or more successor elements.
 */
public class TreeNode extends Tree {
    private Tree parent = null;
    private boolean active = true;
    private List<Tree> children = new ArrayList<Tree>();
    private Shape shape;

    public TreeNode(Shape shape) {
        this.shape = shape;
    }

    @Override
    public BlockCollection derive() {
        BlockCollection c = new BlockCollection();
        for (Tree child : children) {
            BlockCollection childCollection = child.derive();
            c.merge(childCollection);
        }
        return c;
    }

    @Override
    public void setParent(Tree parent) {
        this.parent = parent;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void add(Tree child) {
        child.setParent(this);
        children.add(child);
    }

    public void addChildren(Collection<Tree> cs) {
        for (Tree t : cs) {
            t.setParent(this);
        }
        this.children.addAll(cs);
    }

    public Tree getParent() {
        return parent;
    }

    public List<Tree> getChildren() {
        return children;
    }

    public Shape getShape() {
        return shape;
    }

    public List<TreeNode> findActiveNodes() {
        List<TreeNode> retVal = new ArrayList<TreeNode>();
        if (active) {
            retVal.add(this);
        }
        for (Tree t : children) {
            if (t instanceof TreeNode) {
                retVal.addAll(t.findActiveNodes());
            }
        }
        return retVal;
    }
}
