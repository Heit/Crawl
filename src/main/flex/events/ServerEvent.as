package events{

    import  flash.events.Event;

    public class ServerEvent extends Event{

        public static const MESSAGE:String = "messageEvent";

        public static const CRAWL_DONE:String = "crawlDoneEvent";

        public static const SEARCH_DONE:String = "searchDoneEvent";

        public static const HIGHLIGHT_DONE:String = "hightlightDoneEvent";

        public static const MESSAGE_ERROR:String = "messageErrorEvent";

        public static const SERVER_ERROR:String = "serverErroeEvent";

        public var data:Object;

        public function ServerEvent(type:String, bubbles:Boolean = false, cancelable:Boolean = false) {
            super(type, bubbles, cancelable);
        }
    }
}