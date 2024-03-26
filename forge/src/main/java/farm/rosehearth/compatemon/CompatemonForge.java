package farm.rosehearth.compatemon;

import farm.rosehearth.compatemon.network.client.CompatemonNetworkClient;
import farm.rosehearth.compatemon.util.RunnableReloader;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.server.ServerLifecycleHooks;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Mod(Compatemon.MODID)
@Mod.EventBusSubscriber()
public class CompatemonForge implements CompatemonImplementation{
    
    public CompatemonForge(){
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        MinecraftForge.EVENT_BUS.addListener(this::reloads);
        MinecraftForge.EVENT_BUS.addListener(this::reloadConfigs);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedInEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onClientJoined);
    }
    
    /**
     * Initializes the mod
     * @param event
     */
    @SubscribeEvent
    public  void init(FMLCommonSetupEvent event){
        Compatemon.preInitialize(this);
        Compatemon.init();
    }
    
    /**
     *
     * @return FORGE
     */
    @NotNull
    @Override
    public ModAPI getModAPI() {
        return ModAPI.FORGE;
    }
    
    /**
     *
     * @param modID
     * @return true if the modID is installed
     */
    @Override
    public boolean isModInstalled(@NotNull String modID) {
        return ModList.get().isLoaded(modID);
    }
    
    @Override
    public void postCommonInitialization() {
        CompatemonForgeKotlin.INSTANCE.postCommonInit();
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public void reloads(AddReloadListenerEvent e) {
        e.addListener(RunnableReloader.of(() -> MinecraftForge.EVENT_BUS.post(new CompatemonReloadEvent())));
        Compatemon.loadConfigs(false);
    }
    
    @SubscribeEvent
    public void reloadConfigs(CompatemonReloadEvent event){
    }
    
    @Override
    public void registerEvents() {
        // In Forge, Subscribe Events are handled using the annotation @SubscribeEvent.
        // In Fabric, call them in this function.
    }
    
    @NotNull
    @Override
    public String persistentDataKey() {
        return "ForgeData";
    }
    
    
    @SubscribeEvent
    public void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.getEntity() instanceof ServerPlayer player)
        {
            Compatemon.onPlayerJoinServer(player);
        }
    }
    
    @SubscribeEvent
    public void onClientJoined(ClientPlayerNetworkEvent.LoggingIn event){
        Compatemon.networkClient.onJoinWorld();
    }
    @Override
    public @NotNull Environment environment()  {
        if (FMLEnvironment.dist.isClient())
            return Environment.CLIENT;
        else
            return Environment.SERVER;
    }
    
    @Nullable
    @Override
    public MinecraftServer server() {
        return ServerLifecycleHooks.getCurrentServer();
    }
    
    public static class CompatemonReloadEvent extends Event {}
    
}
