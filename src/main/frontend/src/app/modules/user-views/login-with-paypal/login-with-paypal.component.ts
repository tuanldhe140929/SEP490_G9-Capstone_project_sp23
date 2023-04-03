import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SellerService } from 'src/app/services/seller.service';

@Component({
  selector: 'app-login-with-paypal',
  templateUrl: './login-with-paypal.component.html',
  styleUrls: ['./login-with-paypal.component.css']
})
export class LoginWithPaypalComponent {
  paypalEmail: string = '';

  constructor(private http: HttpClient, private sellerService: SellerService) { 


  }
 onSubmit() {
  // validate email format
  if (!this.isValidEmail(this.paypalEmail)) {
    alert("Invalid email format");
    return;
  }

  // check if email is already used by another seller
  this.sellerService.getSellerByPaypalEmail(this.paypalEmail).subscribe(
    (seller) => {
      if (seller != null) {
        alert("Email is already used by another seller");
      } else {
        // create new seller with provided paypal email
        this.sellerService.createNewSeller(this.paypalEmail).subscribe(
          (seller) => {
            alert("Paypal email added successfully");
          },
          (error) => {
            alert("Error occurred while adding paypal email");
            console.log(error)
          }
        );
      }
    },
    (error) => {
      alert("Error occurred while checking email availability");
    }
  );
}
isValidEmail(email: string): boolean {
  // Email validation regex pattern
  const emailPattern = /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/i;
  return emailPattern.test(email);
}
}
