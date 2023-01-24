import { Component, ElementRef, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, Validators } from '@angular/forms';
import { User } from 'src/app/DTOS/User';
import { AuthService } from 'src/app/services/auth.service';
import { ManageProductService } from 'src/app/services/manage-product.service';
import { StorageService } from '../../../services/storage.service';

@Component({
  selector: 'app-new-product',
  templateUrl: './new-product.component.html',
  styleUrls: ['./new-product.component.css']
})
export class NewProductComponent implements OnInit {

  constructor(private ElByClassName: ElementRef,private storageService: StorageService, private formBuilder: FormBuilder, private manageProductService: ManageProductService) { }

  currentUser: User = new User();

  productUrl = "";
  newProductForm = this.formBuilder.group({
    "title": ['', [Validators.required]],
    productUrl: new FormControl('', [Validators.required]),
    "description": ['', [Validators.required]],
    "classification": ['', [Validators.required]],
    "price": ['', [Validators.required, Validators.min(1000), Validators.max(10000000)]],
    file: new FormControl('', [Validators.required]),
    fileSource: new FormControl('', [Validators.required])
  });

  ngOnInit(): void {

    let email = this.storageService.getAuthResponse().email;
    this.getCurrentUser(email);
  }

  addProduct() {
    this.newProductForm.value.productUrl = this.productUrl.toLowerCase().trim();
    if (this.newProductForm.valid) {
      console.log(this.newProductForm.value);
    }
  }

  getCurrentUser(email: string) {
    this.manageProductService.getCurrentUserInfo(email).subscribe((data) => {
      this.currentUser = data;
      console.log(this.currentUser);
    }, (err) => {
      console.log(err);
    })
  }

  onChoosePayment($event: { target: any; srcElement: any; }) {
    const paid = (<HTMLElement>this.ElByClassName.nativeElement).querySelector(
      '.paid'
    );

     const noPayment = (<HTMLElement>this.ElByClassName.nativeElement).querySelector(
      '.no_payment'
    );
    let clickedElement = $event.target || $event.srcElement;
    if (clickedElement.nodeName === "BUTTON") {
      let isCertainButtonAlreadyActive = clickedElement.parentElement.querySelector(".active");
      // if a Button already has Class: .active
      if (isCertainButtonAlreadyActive) {
        isCertainButtonAlreadyActive.classList.remove("active");
      }
      clickedElement.className += " active";

      if (clickedElement.className == "payment_mode_paid active") {
        if (paid != null) {
          paid.setAttribute("style", "display:block;");
        }
        if (noPayment != null) {
          noPayment.setAttribute("style", "display:none;");
        }
      }
      if (clickedElement.className == "payment_mode_no_paid active") {
        if (paid != null) {
          paid.setAttribute("style", "display:none; ");
        }
        if (noPayment != null) {
          noPayment.setAttribute("style", "display: block;");
        }
      }
    }
  }

  onPriceChange($event: { target: any; srcElement: any; keyCode: any; }): void {
    var minPrice = 1000;
    const keyCode = $event.keyCode;

    const excludedKeys = [8, 37, 39, 46];
    console.log(keyCode);

    if (!((keyCode >= 48 && keyCode <= 57) ||
      (keyCode >= 96 && keyCode <= 105) ||
      (excludedKeys.includes(keyCode)))) {
     
    }
  }
}
