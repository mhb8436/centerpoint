import UIKit

class AddMeetDialogController: UIViewController {
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.view.addSubview(nameTextField)
        
        nameTextField.centerYAnchor.constraint(equalTo:self.view.centerYAnchor).isActive = true
        nameTextField.leadingAnchor.constraint(equalTo:self.view.leadingAnchor, constant: 10).isActive = true
        nameTextField.trailingAnchor.constraint(equalTo:self.view.trailingAnchor, constant: -10).isActive = true
    }
    
    let nameTextField:UITextField = {
        let textField = UITextField()
        textField.font = UIFont.boldSystemFont(ofSize: 20)
//        textField.textColor = .black
        textField.translatesAutoresizingMaskIntoConstraints = false
        return textField
    }()
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @objc func endEditing() {
        view.endEditing(true)
    }
}

extension AddMeetDialogController: UITextFieldDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        endEditing()
        return true
    }
}
