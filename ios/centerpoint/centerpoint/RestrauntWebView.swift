import UIKit
import WebKit

class RestrauntWebView : UIViewController, WKUIDelegate {
    var webView: WKWebView!
    var url : String!
    
    override func loadView() {
        let webConfiguration = WKWebViewConfiguration()
        webView = WKWebView(frame: .zero, configuration: webConfiguration)
        webView.uiDelegate = self
//        webView.frame = CGRect(x:0, y:45, width: self.view.frame.width, height:self.view.frame.height - 45)
        view = webView
//        self.view.addSubview(webView)
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
//        var button: UIButton = UIButton()
//        button.setImage(UIImage(named: "close"), for: .normal)
//        button.frame = CGRect(x:0, y:0, width:45, height:45)
//
//        var rightItem:UIBarButtonItem = UIBarButtonItem()
//        rightItem.customView = button
//        self.navigationItem.rightBarButtonItem = rightItem

        if self.url != nil && !self.url.isEmpty {
            let myURL = URL(string:self.url)
            let myRequest = URLRequest(url: myURL!)
            webView.load(myRequest)
            
        }
        
    }
}
