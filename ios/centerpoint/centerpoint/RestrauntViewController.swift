import UIKit
import ALRT


class RestrauntViewController: UITableViewController {
    
    private static let CellIdentifier = "Cell"
    
    public var restraunts : [RecommendPlaceModel] = []
    var mapViewController: MapViewController?
    
    override func viewDidLoad() {
        super.viewDidLoad()        
        self.title = "Restraunt"
//        print("RestrauntViewController", "vieDidLoad", restraunts)
        self.tableView.register(RestrauntTableViewCell.self, forCellReuseIdentifier: RestrauntViewController.CellIdentifier)
    }
    
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
//        print("RestrauntViewController", "numberOfRowsInSection", restraunts.count)
        return restraunts.count;
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: RestrauntViewController.CellIdentifier, for: indexPath) as! RestrauntTableViewCell;
        cell.restraunt = restrauntAtIndex(indexPath);
        return cell;
    }
    
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }

    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
    }
    
    
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 100;
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let restraunt = restrauntAtIndex(indexPath)
        mapViewController?.onPinRestraunt(restraunt:restraunt)
        navigationController?.popViewController(animated: true)
    }
    private func restrauntAtIndex(_ indexPath: IndexPath) -> RecommendPlaceModel {
        return restraunts[indexPath.row]
    }
    
}


