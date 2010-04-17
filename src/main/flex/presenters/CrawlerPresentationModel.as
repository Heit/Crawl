package presenters{
import flash.events.IEventDispatcher;

import model.*;

import flash.events.Event;
import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;
import mx.controls.Alert;

public class CrawlerPresentationModel extends EventDispatcher {

  public static const INIT:String = "initEnabled";

  public static const CRAWL:String = "crawlEnabled";

  public static const SEARCH:String = "searchEnabled";

  public static const HIGHLIGHT:String = "highlightEnabled";

  private var _state:String = INIT;

  public var _documents:ArrayCollection = new ArrayCollection();

  public var _results:ArrayCollection = new ArrayCollection();

  public var _bestFragment:HlResult = new HlResult();

  private var dispatcher:IEventDispatcher;

  public function CrawlerPresentationModel(dispatcher:IEventDispatcher) {
    this.dispatcher = dispatcher;
  }


  [Bindable(event="modelChange")]
  public function get documents():ArrayCollection {
    return _documents;
  }

  public function set documents(value:ArrayCollection):void {
    _documents = value;
    if (value != null && value.length != 0) {
      _state = CRAWL;
      dispatchEvent(new Event("stateChange"));
      dispatchEvent(new Event("modelChange"));
    }
  }

  [Bindable(event="modelChange")]
  public function get results():ArrayCollection {
    return _results;
  }

  public function set results(value:ArrayCollection):void {
    _results = value;
    if (value != null && value.length != 0) {
      _state = SEARCH;
      dispatchEvent(new Event("stateChange"));
      dispatchEvent(new Event("modelChange"));
    }
  }

  [Bindable(event="modelChange")]
  public function get bestFragment():HlResult {
    return _bestFragment;
  }

  public function set bestFragment(value:HlResult):void {
    _bestFragment = value;
    if (value != null) {
       _state = HIGHLIGHT;
      dispatchEvent(new Event("stateChange"));
      dispatchEvent(new Event("modelChange"));
    }
  }

  [Bindable(event="stateChange")]
  public function get state():String {
    return _state;
  }

  public function set state(value:String):void {
    _state = value;
  }
}
}