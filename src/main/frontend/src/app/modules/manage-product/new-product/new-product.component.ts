import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, Validators } from '@angular/forms';
import { User } from 'src/app/interfaces/User';
import { AuthService } from 'src/app/services/auth.service';
import { ManageProductService } from 'src/app/services/manage-product.service';
import { StorageService } from '../../../services/storage.service';

@Component({
  selector: 'app-new-product',
  templateUrl: './new-product.component.html',
  styleUrls: ['./new-product.component.css']
})
export class NewProductComponent implements OnInit {
  user:any;
  productName = "";
  newProductForm = this.formBuilder.group({
    name: new FormControl(),
    "description": ['', [Validators.required]]
  });
  constructor(private storageService: StorageService, private formBuilder: FormBuilder, private manageProductService:ManageProductService) { }
  ngOnInit(): void {
	  
    let email = this.storageService.getAuthResponse().email;
    this.getCurrentUser(email);
  }

  addProduct() {
    this.newProductForm.value.name = this.productName;
    if (this.newProductForm.valid) {
      console.log(this.newProductForm.value);
    }
  }
  
  getCurrentUser(email:string){
	  this.manageProductService.getCurrentUserInfo(email).subscribe((data)=>{
		  this.user = data;
	  },(err)=>{
		  
	  })
  }
}
