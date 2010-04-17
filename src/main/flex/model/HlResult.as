package model{
import flash.events.EventDispatcher;

public class HlResult extends EventDispatcher {

  private var _data:String;


  public function HlResult() {
  }

  [Bindable]
  public function get data():String {
    return _data;
  }

  public function set data(value:String):void {
    _data = value;
  }
}
}