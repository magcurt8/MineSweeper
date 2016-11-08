package com.example.maggie.minesweeper.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.maggie.minesweeper.MainActivity;
import com.example.maggie.minesweeper.R;
import com.example.maggie.minesweeper.model.MineSweeperModel;

import java.text.AttributedCharacterIterator;

/**
 * Created by Maggie on 2/25/16.
 */
public class MineSweeperView extends View {

    private Paint paintBackground;
    private Paint paintLine;
    private Paint paintMine;
    private Paint paintNumber;
    private Paint paintFlag;
    public boolean mineExist = false;
    public boolean mineNear = false;
    public boolean flagMode = false;
    public int flagMine = 0;

    //constructor to create all paint values
    public MineSweeperView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintBackground = new Paint();
        paintBackground.setColor(Color.BLACK);
        paintBackground.setStyle(Paint.Style.FILL);

        paintMine = new Paint();
        paintMine.setColor(Color.BLUE);
        paintMine.setStyle(Paint.Style.FILL);

        paintLine = new Paint();
        paintLine.setColor(Color.WHITE);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);

        paintNumber = new Paint();
        paintNumber.setColor(Color.RED);
        paintNumber.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);

        paintFlag = new Paint();
        paintFlag.setColor(Color.RED);
        paintFlag.setStyle(Paint.Style.FILL);
        MineSweeperModel.getInstance().setMines();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGameArea(canvas);
        //if current location clicked
        if (mineNear) {
            drawNumbers(canvas);
        }
        if (endGame()) {
            drawMine(canvas);
        }
        drawFlags(canvas);
    }

    //draw original game board without mines or values
    private void drawGameArea(Canvas canvas) {
        //border
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintLine);
        // two horizontal lines
        canvas.drawLine(0, getHeight() / 5, getWidth(), getHeight() / 5,
                paintLine);
        canvas.drawLine(0, 2 * getHeight() / 5, getWidth(), 2 * getHeight() / 5, paintLine);
        canvas.drawLine(0, 3 * getHeight() / 5, getWidth(), 3 * getHeight() / 5, paintLine);
        canvas.drawLine(0, 4 * getHeight() / 5, getWidth(), 4 * getHeight() / 5, paintLine);

        // two vertical lines
        canvas.drawLine(getWidth() / 5, 0, getWidth() / 5, getHeight(),
                paintLine);
        canvas.drawLine(2 * getWidth() / 5, 0, 2 * getWidth() / 5, getHeight(),
                paintLine);
        canvas.drawLine(3 * getWidth() / 5, 0, 3 * getWidth() / 5, getHeight(), paintLine);
        canvas.drawLine(4 * getWidth() / 5, 0, 4 * getWidth() / 5, getHeight(), paintLine);
    }
    //VALUE AT MODEL[X][Y] SETS TO FLAG WHEN FLAG MODE IS ON AND THEN THE MINE IS REMOVED SO THE USER LOSES EVEN THOUGH THEY WERE CORRECT
    //GAME LOGIC WORKS PERFECTLY WITHOUT FLAG MODE ON
    //ONLY CHANGES WHEN FIRST FLAG IS PLACED

    public boolean onTouchEvent(MotionEvent event) {
        int tX = ((int) event.getX()) / (getWidth() / 5);
        int tY = ((int) event.getY()) / (getHeight() / 5);

        if (tX < 5 && tY < 5) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (flagMode && flagMine <= 2) {
                    MineSweeperModel.getInstance().setFieldContent(tX, tY, MineSweeperModel.FLAG);
                    flagMine++;
                }
                if (flagMode) {
                    if (flagMine == 3) {
                        if (MineSweeperModel.getInstance().getFieldContent(tX, tY) == 1) {
                            ((MainActivity) getContext()).showSnackbarMessage(getContext().getString(R.string.winMessage));
                            System.out.println("You win");
                            winGame();
                            invalidate();
                        }
                    }
                    if (MineSweeperModel.getInstance().getFieldContent(tX, tY) != 1) {
                        mineExist = false;
                        ((MainActivity) getContext()).showSnackbarMessage(getContext().getString(R.string.loseMessage));
                        flagEndGame();
                        invalidate();
                    }
                } else {
                    if (MineSweeperModel.getInstance().getFieldContent(tX, tY) == MineSweeperModel.MINE) {
                        mineExist = true;
                        endGame();
                        ((MainActivity) getContext()).showSnackbarMessage(getContext().getString(R.string.loseMessage));
                        invalidate();
                    } else if (!(MineSweeperModel.getInstance().getFieldContent(tX, tY) == MineSweeperModel.MINE)) {
                        MineSweeperModel.getInstance().mineNear(tX, tY);
                        mineNear = true;
                        invalidate();
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void drawMine(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {

                float centerX = i * getWidth() / 5 + getWidth() / 10;
                float centerY = j * getHeight() / 5 + getHeight() / 10;

                if (MineSweeperModel.getInstance().getFieldContent(i, j) == MineSweeperModel.MINE) {
                    int radius = getHeight() / 10 - 2;
                    canvas.drawCircle(centerX, centerY, radius, paintMine);
                }
            }
        }
    }

    public void drawNumbers(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                float centerX = i * getWidth() / 5 + getWidth() / 10;
                float centerY = j * getHeight() / 5 + getHeight() / 10;


                if (MineSweeperModel.getInstance().getFieldContent(i, j) == MineSweeperModel.FLAG) {
                    drawFlags(canvas);
                }
                if (mineNear) {
                    if (MineSweeperModel.getInstance().getFieldContent(i, j) == MineSweeperModel.ZERO) {
                        canvas.drawText("0", centerX, centerY, paintNumber);
                    } else if (MineSweeperModel.getInstance().getFieldContent(i, j) == MineSweeperModel.ONE) {
                        canvas.drawText("1", centerX, centerY, paintNumber);
                    } else if (MineSweeperModel.getInstance().getFieldContent(i, j) == MineSweeperModel.TWO) {
                        canvas.drawText("2", centerX, centerY, paintNumber);
                    } else if (MineSweeperModel.getInstance().getFieldContent(i, j) == MineSweeperModel.THREE) {
                        canvas.drawText("3", centerX, centerY, paintNumber);
                    }
                }
            }
        }
    }

    public void drawFlags(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                //float centerX = i * getWidth() / 5 + getWidth() / 10;
                //float centerY = j * getHeight() / 5 + getHeight() / 10;
                if (MineSweeperModel.getInstance().isFlag(i, j)) {
                    canvas.drawLine(i * getWidth() / 5, j * getHeight() / 5,
                            (i + 1) * getWidth() / 5,
                            (j + 1) * getHeight() / 5, paintFlag);
                    canvas.drawLine((i + 1) * getWidth() / 5, j * getHeight() / 5,
                            i * getWidth() / 5,
                            (j + 1) * getHeight() / 5, paintFlag);
                }
            }
        }
    }

    public void setFlag(boolean flag) {
        this.flagMode = flag;
    }

    public boolean endGame() {
        if (mineExist) {
            return true;
        }
        return false;
    }

    public boolean flagEndGame() {
        if (!mineExist) {
            return true;
        }
        return false;
    }

    public boolean winGame() {
        if (flagMine == 3) {
            return true;
        }
        return false;
    }

    public void clearGameField() {
        MineSweeperModel.getInstance().resetModel();
        invalidate();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }
}
