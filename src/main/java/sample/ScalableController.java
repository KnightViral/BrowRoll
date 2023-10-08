package sample;

import javafx.scene.layout.Region;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;


import java.util.concurrent.atomic.AtomicReference;

public interface ScalableController {

    String SETTINGS_FILE_NAME = "scale_setting";

    AtomicReference<Double> scaleFactor = new AtomicReference<>((double) 1);
    AtomicReference<Stage> stage = new AtomicReference<>(null);

    void updateUIScale(boolean initListeners);

    default double getScaleFactor() {
        return scaleFactor.get();
    }

    default void setScaleFactor(double value) {
        scaleFactor.set(value);
    }

    default Stage getStage() {
        return stage.get();
    }

    default void setStage(Stage value) {
        stage.set(value);
    }

    default void initScale(Region region) {
        Scale scale = new Scale(scaleFactor.get(), scaleFactor.get());
        scale.setPivotX(0);
        scale.setPivotY(0);
        region.getTransforms().setAll(scale);
    }

    default void applyScale(Region region) {
        region.setPrefWidth((stage.get().getWidth() - 16) / scaleFactor.get());
        region.setPrefHeight((stage.get().getHeight() - 39) / scaleFactor.get());
    }

}
