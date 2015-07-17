package matteroverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementTextFieldFiltered;
import cofh.lib.util.helpers.MathHelper;
import matteroverdrive.Reference;
import matteroverdrive.container.IButtonHandler;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.MOGuiBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

/**
 * Created by Simeon on 5/3/2015.
 */
public class ElementIntegerField extends ElementBaseGroup implements IButtonHandler
{
    MOElementTextField numberField;
    MOElementButtonScaled incBtn;
    MOElementButtonScaled decBtn;
    int min;
    int max;

    public ElementIntegerField(MOGuiBase gui, int posX, int posY, int width, int height,int min,int max) {
        super(gui, posX, posY, width, height);

        numberField = new MOElementTextField(gui,18,4,width - 18 - 12,height);
        numberField.setBackground(new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "field_over.png"), 30, 18).setOffsets(5, 5, 5, 5));
        numberField.setTextOffset(4, 4);

        incBtn = new MOElementButtonScaled(gui,this,0,0,"Inc",16,height);
        incBtn.setNormalTexture(new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "button_normal_left.png"),10,18).setOffsets(5,2,5,5));
        incBtn.setText("+");

        decBtn = new MOElementButtonScaled(gui,this,width - 16,0,"Dec",16,height);
        decBtn.setNormalTexture(new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "button_normal_right.png"), 10, 18).setOffsets(2, 5, 5, 5));
        decBtn.setText("-");
        this.min = min;
        this.max = max;
    }

    public ElementIntegerField(MOGuiBase gui, int posX, int posY, int width, int height)
    {
        this(gui, posX, posY, width, height, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public void init()
    {
        super.init();

        addElement(numberField);
        addElement(incBtn);
        addElement(decBtn);
    }

    public ElementIntegerField(MOGuiBase gui, int posX, int posY) {
        this(gui, posX, posY, 120, 18, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public int getNumber()
    {
        if (numberField.getText().isEmpty())
            return 0;

        int number = 0;

        try {
            number = Integer.parseInt(numberField.getText());
        }catch (Exception e)
        {

        }

        MathHelper.clampI(number, min, max);
        return number;
    }

    public void setNumber(int number)
    {
        number = MathHelper.clampI(number,min,max);
        numberField.setText(Integer.toString(number));
    }

    @Override
    public void handleElementButtonClick(String buttonName, int mouseButton)
    {
        super.handleElementButtonClick(buttonName,mouseButton);

        if (buttonName == "Inc")
        {
            setNumber(getNumber()+(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 16 : 1));
        }
        else if (buttonName == "Dec")
        {
            setNumber(getNumber()-(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 16 : 1));
        }
    }

    public void setBounds(int min,int max)
    {
        this.min = min;
        this.max = max;
    }
}
