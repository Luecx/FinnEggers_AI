package network.layers;

public class InputLayer extends Layer{

    public InputLayer(int output_depth, int output_width, int output_height) {
        super(output_depth, output_width, output_height);
    }

    public void setInput(double[][][] in) {
        if(!(
                this.OUTPUT_DEPTH != in.length ||
                        this.OUTPUT_WIDTH != in[0].length ||
                        this.OUTPUT_HEIGHT != in[0][0].length)){
            this.output_values = in;
        }
    }

    @Override
    protected void on_build() throws Exception{

    }

    @Override
    protected void calculateOutputDimensions() {

    }

    @Override
    public void calculate() {

    }

    @Override
    public void backprop_error() {

    }

    @Override
    public void update_weights(double eta) {

    }
}
