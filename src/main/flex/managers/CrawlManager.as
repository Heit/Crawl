package managers {
import events.ServerEvent;

import flash.events.Event;
import flash.events.EventDispatcher;
import flash.events.IEventDispatcher;

import model.HlResult;

import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.utils.ObjectProxy;

public class CrawlManager extends EventDispatcher{

  private var service:Service;

  private var dispatcher:IEventDispatcher;

  public var documentIndex:int;

  private var _searchResults:ArrayCollection;

  private var _crawledDocumets:ArrayCollection;

  private var _highlight:HlResult;

  [Bindable(event = "documentsChange")]
  public function get crawledDocumets():ArrayCollection {
    return _crawledDocumets;
  }

  [Bindable(event = "resultsChange")]
  public function get searchResults():ArrayCollection {
    return _searchResults;
  }

  [Bindable(event = "highlightChange")]
  public function get highlight():HlResult {
    return _highlight;
  }

  public function CrawlManager(dispatcher:IEventDispatcher, service:Service) {
    this.dispatcher = dispatcher;
    this.service = service;
  }

  public function getDocuments(host:String, baseurl:String):void {
    service.crawlSvc.addEventListener(ServerEvent.CRAWL_DONE, processDocuments);
    service.crawlSvc.addEventListener(ServerEvent.SERVER_ERROR, errorHandler);
    service.getDocuments(host, baseurl);

    function processDocuments(event:ServerEvent):void {
      _crawledDocumets = new ArrayCollection();
      service.crawlSvc.removeEventListener(ServerEvent.CRAWL_DONE, processDocuments);
      service.crawlSvc.removeEventListener(ServerEvent.SERVER_ERROR, errorHandler);
      var documents:ArrayCollection = event.data as ArrayCollection;
      for each (var document:Object in documents) {
        _crawledDocumets.addItem(document);
      }
      documentIndex = crawledDocumets.length;
      dispatchEvent(new Event("documentsChange"));
      Alert.show("Проиндексирован: " + crawledDocumets.length + " документ");
    }

    function errorHandler(event:ServerEvent):void {
      trace(event);
      service.crawlSvc.removeEventListener(ServerEvent.CRAWL_DONE, processDocuments);
      service.crawlSvc.removeEventListener(ServerEvent.SERVER_ERROR, errorHandler);
    }
  }

  public function doSearch(searchQuery:String):void {
    service.crawlSvc.addEventListener(ServerEvent.SEARCH_DONE, processSearched);
    service.crawlSvc.addEventListener(ServerEvent.SERVER_ERROR, errorHandler);
    service.doSearch(searchQuery);

    function processSearched(event:ServerEvent):void {
//      searchResults.removeAll();
      _searchResults = new ArrayCollection();
      service.crawlSvc.removeEventListener(ServerEvent.SEARCH_DONE, processSearched);
      service.crawlSvc.removeEventListener(ServerEvent.SERVER_ERROR, errorHandler);
      var documents:ArrayCollection = event.data as ArrayCollection;
      for each (var document:Object in documents) {
        _searchResults.addItem(document);
      }
      dispatchEvent(new Event("resultsChange"));
    }

    function errorHandler(event:ServerEvent):void {
      trace(event);
      service.crawlSvc.removeEventListener(ServerEvent.SEARCH_DONE, processSearched);
      service.crawlSvc.removeEventListener(ServerEvent.SERVER_ERROR, errorHandler);
    }
  }

  public function highlightResults(query:String, path:String):void {
    service.crawlSvc.addEventListener(ServerEvent.HIGHLIGHT_DONE, processSearched);
    service.crawlSvc.addEventListener(ServerEvent.SERVER_ERROR, errorHandler);
    service.highlightResults(query, path);

    function processSearched(event:ServerEvent):void {
      _highlight = new HlResult();
      service.crawlSvc.removeEventListener(ServerEvent.HIGHLIGHT_DONE, processSearched);
      service.crawlSvc.removeEventListener(ServerEvent.SERVER_ERROR, errorHandler);
      _highlight.data = event.data as String;
      dispatchEvent(new Event("highlightChange"));
    }

    function errorHandler(event:ServerEvent):void {
      trace(event);
      service.crawlSvc.addEventListener(ServerEvent.HIGHLIGHT_DONE, processSearched);
      service.crawlSvc.removeEventListener(ServerEvent.SERVER_ERROR, errorHandler);
    }
  }

}
}