package events{

    import flash.events.Event;

    public class CrawlEvent extends Event{

        public static const CRAWL:String = "crawlEvent";

        public static const SEARCH:String = "searchEvent";

        public static const HIGHLIGHT:String = "highlightEvent";

        public var host:String;

        public var baseurl:String;

        public var query:String;

        public var path:String;
        
        public function CrawlEvent(type:String, bubbles:Boolean = false, cancelable:Boolean = false) {
            super(type, bubbles, cancelable);
        }
    }
}