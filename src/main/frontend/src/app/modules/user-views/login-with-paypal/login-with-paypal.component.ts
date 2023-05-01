import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SellerService } from 'src/app/services/seller.service';
import { UserService } from 'src/app/services/user.service';
import { AccountService } from 'src/app/services/account.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-login-with-paypal',
  templateUrl: './login-with-paypal.component.html',
  styleUrls: ['./login-with-paypal.component.css']
})
export class LoginWithPaypalComponent {
  paypalEmail: string = '';
  isPopupShown = false;
  constructor(private http: HttpClient, private sellerService: SellerService,
     private accountService: AccountService, private router: Router) { 


  }
  
 onSubmit() {
  // validate email format
  if (!this.isValidEmail(this.paypalEmail)) {
    alert("sai định dạng email");
    return;
  }
  // check if email is already used by another seller
  this.sellerService.getSellerByPaypalEmail(this.paypalEmail).subscribe(
    (seller) => {
      if (seller != null) {
        alert("Email đã được sử dụng bởi người dùng khác");
      } else { 
       
        //popUp if chose close stop and close the popup if chose continue createNewSeller and logout
        // create new seller with provided paypal email
        this.showPopup();
      }
    },
    (error) => {
      alert("sai định dạng email");
    }
  );
}
showPopup() {
  this.isPopupShown = true;
}

hidePopup() {
  this.isPopupShown = false;
}
isValidEmail(email: string): boolean {
  // Email validation regex pattern
  const emailPattern = /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/i;
  return emailPattern.test(email);
}
logout(): void {
  this.sellerService.createNewSeller(this.paypalEmail).subscribe( 
    (seller) => {
     
    },
    (error) => {
      alert("Bạn đã có tài khoản paypal email");
      console.log(error)
    }
  );
  this.accountService.logout().subscribe(
    () => {
      localStorage.clear();
      this.router.navigate(['/login']);
    },
    (error) => {
      console.error('Logout failed', error);
      localStorage.clear();
    }
  );
}
}
