/*
 * Copyright 2014 MovingBlocks
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

package org.terasology.grammar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.terasology.grammar.model.Building;
import org.terasology.grammar.model.BuildingRasterizer;
import org.terasology.grammar.model.ShapeGrammar;

/**
 * Setup a simple debug environment
 * @author Martin Steiger
 */
public class ShapeGrammarMain
{
    /**
     * @param args (ignored)
     */
    public static void main(String[] args)
    {
        // Create and set up the window.
        final JFrame frame = new JFrame("Shape Grammar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new JComponent()
        {
            private static final long serialVersionUID = -3019274194814342555L;

            @Override
            protected void paintComponent(final Graphics g1)
            {
                super.paintComponent(g1);
                final Graphics2D g = (Graphics2D) g1;
                int scale = 10;
                g.scale(scale, scale);

                // draw grid
                g.setColor(Color.LIGHT_GRAY);
                g.setStroke(new BasicStroke(0f));
                for (int i = 0; i < getWidth() / scale + 1; i++) {
                    g.drawLine(i, 0, i, getHeight());
                }
                for (int i = 0; i < getHeight() / scale + 1; i++) {
                    g.drawLine(0, i, getWidth(), i);
                }

                g.translate(0.5, 0.5);
                g.setStroke(new BasicStroke());
                g.setColor(Color.BLACK);

                Building blg = new ShapeGrammar().create(new Rectangle(3, 3, 60, 33), new Point(0, 1));
                BuildingRasterizer.raster(g, blg);
            }

        });

        // repaint scene every 500ms - for debugging
        Timer t = new Timer();
        t.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                frame.repaint();

            }
        }, 500, 500);

        frame.setLocation(600, 200);
        frame.setSize(700, 450);
        frame.setVisible(true);
    }
}
