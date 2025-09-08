package dev.dong4j.zeka.kernel.common.support;

import org.jetbrains.annotations.Contract;

/**
 * <p>Description: 图片操作类 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:18
 * @since 1.0.0
 */
public class ImagePosition {

    /** 图片顶部. */
    public static final int TOP = 32;
    /** 图片中部. */
    public static final int MIDDLE = 16;
    /** 图片底部. */
    public static final int BOTTOM = 8;
    /** 图片左侧. */
    public static final int LEFT = 4;
    /** 图片居中. */
    public static final int CENTER = 2;
    /** 图片右侧. */
    public static final int RIGHT = 1;
    /** 横向边距,靠左或靠右时和边界的距离. */
    private static final int PADDING_HORI = 6;
    /** 纵向边距,靠上或靠底时和边界的距离. */
    private static final int PADDING_VERT = 6;
    /** 图片中盒[左上角]的x坐标. */
    private final int boxPosX;
    /** 图片中盒[左上角]的y坐标. */
    private final int boxPosY;

    /**
     * Instantiates a new image position.
     *
     * @param width     the width
     * @param height    the height
     * @param boxWidth  the box width
     * @param boxHeight the box height
     * @param style     the style
     * @since 1.0.0
     */
    @Contract(pure = true)
    public ImagePosition(int width, int height, int boxWidth, int boxHeight, int style) {
        switch (style & 7) {
            case LEFT:
                this.boxPosX = PADDING_HORI;
                break;
            case RIGHT:
                this.boxPosX = width - boxWidth - PADDING_HORI;
                break;
            case CENTER:
            default:
                this.boxPosX = (width - boxWidth) / 2;
        }
        switch (style >> 3 << 3) {
            case TOP:
                this.boxPosY = PADDING_VERT;
                break;
            case MIDDLE:
                this.boxPosY = (height - boxHeight) / 2;
                break;
            case BOTTOM:
            default:
                this.boxPosY = height - boxHeight - PADDING_VERT;
        }
    }

    /**
     * Gets the x.
     *
     * @return the x
     * @since 1.0.0
     */
    public int getX() {
        return this.getX(0);
    }

    /**
     * Gets the x.
     *
     * @param x 横向偏移
     * @return the x
     * @since 1.0.0
     */
    public int getX(int x) {
        return this.boxPosX + x;
    }

    /**
     * Gets the y.
     *
     * @return the y
     * @since 1.0.0
     */
    public int getY() {
        return this.getY(0);
    }

    /**
     * Gets the y.
     *
     * @param y 纵向偏移
     * @return the y
     * @since 1.0.0
     */
    public int getY(int y) {
        return this.boxPosY + y;
    }

}
