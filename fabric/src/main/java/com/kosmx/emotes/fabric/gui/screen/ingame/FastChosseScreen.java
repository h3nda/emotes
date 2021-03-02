package com.kosmx.emotes.fabric.gui.screen.ingame;

import com.kosmx.emotes.fabric.gui.screen.AbstractControlledModScreen;
import com.kosmx.emotes.fabric.gui.screen.IDrawableImpl;
import com.kosmx.emotes.main.screen.AbstractScreenLogic;
import com.kosmx.emotes.main.screen.IScreenSlave;
import com.kosmx.emotes.main.screen.ingame.FastMenuScreenLogic;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class FastChosseScreen extends AbstractControlledModScreen {
    public FastChosseScreen(Screen parent) {
        super(new TranslatableText("emotecraft.fastmenu"), parent);
    }

    @Override
    protected AbstractScreenLogic<MatrixStack, Screen> newMaster() {
        return new FastMenuScreenLogicImpl(this);
    }

    class FastMenuScreenLogicImpl extends FastMenuScreenLogic<MatrixStack, Screen, Element> implements IScreenHelperImpl{

        protected FastMenuScreenLogicImpl(IScreenSlave screen) {
            super(screen);
        }

        @Override
        protected IScreenSlave<MatrixStack, Screen> newFullScreenMenu() {
            return new FullScreenListImpl(FastChosseScreen.this);
        }

        @Override
        protected com.kosmx.emotes.main.screen.ingame.FastMenuScreenLogic<MatrixStack, Screen, Element>.FastMenuWidget newFastMenuWidget(int x, int y, int size) {
            return new FastMenuWidgetImpl(x, y, size);
        }
        class FastMenuWidgetImpl extends FastMenuWidget implements IDrawableImpl, Element {
            public FastMenuWidgetImpl(int x, int y, int size) {
                super(x, y, size);
            }

            @Override
            public Element get() {
                return this;
            }
        }
    }
}
