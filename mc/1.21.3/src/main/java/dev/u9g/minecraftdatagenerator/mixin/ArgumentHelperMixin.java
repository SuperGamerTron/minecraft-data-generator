package dev.u9g.minecraftdatagenerator.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.command.argument.ArgumentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArgumentHelper.class)
public class ArgumentHelperMixin {
    @Inject(method = "writeArgument", at = @At("TAIL"))
    private static <T extends ArgumentType<?>> void writeArgument(JsonObject json, T type, CallbackInfo ci) {
        JsonArray examples = new JsonArray();
        for (var example : type.getExamples()) {
            examples.add(example);
        }
        json.add("examples", examples);
    }
}
