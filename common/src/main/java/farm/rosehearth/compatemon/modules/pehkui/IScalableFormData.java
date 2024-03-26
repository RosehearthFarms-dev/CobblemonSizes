package farm.rosehearth.compatemon.modules.pehkui;

public interface IScalableFormData {
	
	default float compatemon$getSizeScale(){
		return 1.0f;
	}
	
	default void compatemon$setSizeScale(float sizeScale){}
}
