package dev.u9g.minecraftdatagenerator.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.class_4323;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_4323.class)
public class ArgumentTypesMixin {
    @Inject(method = "method_19893(Lcom/google/gson/JsonObject;Lcom/mojang/brigadier/arguments/ArgumentType;)V", at = @At("TAIL"))
    private static <T extends ArgumentType<?>> void toJson(JsonObject json, T type, CallbackInfo ci) {
        JsonArray examples = new JsonArray();
        for (var example : type.getExamples()) {
            examples.add(example);
        }
        json.add("examples", examples);
    }
}
