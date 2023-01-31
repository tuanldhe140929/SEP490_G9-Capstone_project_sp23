import { HttpClient, HttpEventType } from '@angular/common/http';
import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, Validators } from '@angular/forms';
import { finalize, Subscription } from 'rxjs';
import { Product } from 'src/app/DTOS/Product';
import { User } from 'src/app/DTOS/User';
import { AuthService } from 'src/app/services/auth.service';
import { ManageProductService } from 'src/app/services/manage-product.service';
import { StorageService } from '../../../services/storage.service';

const baseUrl = "http://localhost:9000/private/manageProduct";

@Component({
  selector: 'app-new-product',
  templateUrl: './new-product.component.html',
  styleUrls: ['./new-product.component.css']
})
export class NewProductComponent implements OnInit {

  @Input()
  requiredFileType!: string;

  fileName = '';
  uploadProgress: number = 0;
  uploadSub: Subscription | undefined;

  product: Product;
  
  constructor(private ElByClassName: ElementRef,
    private storageService: StorageService, private formBuilder: FormBuilder,
    private manageProductService: ManageProductService,
    private http: HttpClient) {
		this.product = new Product();
		this.product.id = 1;
	 }

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

  onFileSelected($event: any) {
    const file: File = $event.target.files[0];

    if (file) {
      this.fileName = file.name;
      const form = new FormBuilder();
      const formData = new FormData();
      formData.set("enctype","multipart/form-data");
      formData.append("coverImage", file);
	  formData.append("productId",this.product.id.toString());
	
      const upload$ = this.manageProductService.uploadCoverImage(formData).subscribe(
		  (data) =>{
				this.reset();
				
				if(this.coverImg==null){
					const img = document.createElement("img");
					img.className = "cover_img";
					img.id = "cover_img";
					this.coverImgContainer?.appendChild(img);
				}
				if(this.coverImg!=null){
				this.product.coverImage = data.coverImage;
            	this.coverImg.style.width = "100%";
            	this.coverImg.style.height = "300px";
            	this.coverImg.style.padding = "0";
            	this.coverImg.style.margin = "0";
				}	
				
            	this.manageProductService.getCoverImage(this.product.id).subscribe(
					(data)=>{
						if(this.coverImg){
						this.coverImg.setAttribute('src',URL.createObjectURL(data));
						}
						if(this.UploadBtn){
						this.UploadBtn.className = this.UploadBtn.className.substring(0,this.UploadBtn.className.length-9);
						this.UploadBtn.className = this.UploadBtn.className.concat(' has_upload');
						}
						
					},
					(error)=>{
						console.log(error);
					}
				);
				
				

		  },
		  (error)=>{
			  console.log(error);
		  }
				)}
				
	  }
        
     // this.uploadSub = upload$.subscribe(event => {
     //   if (event.type == HttpEventType.UploadProgress) {
     //     this.uploadProgress = Math.round(100 * ($event.loaded / $event.total));
     //   }
     // })

  cancelUpload() {
    if (this.uploadSub != null) {
      this.uploadSub.unsubscribe();
    }

    this.reset();
  }

  reset() {
    this.uploadProgress = 0;
    //this.uploadSub = null;
  }

  get UploadBtn(){
	  return document.getElementsByClassName('upload_buttons').item(0);
  }	
  get coverImg() {
    return document.getElementById('cover_img');
  }
  
  get coverImgContainer(){
	  return document.getElementById('cover_img_div');
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
