package stickman.model;

public abstract class EntityImpl implements  Entity{

    public String imagePath;
    public double position_X;
    public double position_Y;
    public double width;
    public double height;
    public String size;
    public Entity.Layer layer;

    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    @Override
    public double getXPos() {
        return this.position_X;
    }

    @Override
    public double getYPos() {
        return this.position_Y;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    public void setXpos(double X){
        this.position_X = X;
    }


    public void setYpos(double Y){
        this.position_Y = Y;
    }
}
