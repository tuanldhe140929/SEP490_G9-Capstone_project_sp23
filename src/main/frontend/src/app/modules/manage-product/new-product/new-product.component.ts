import { HttpClient, HttpEventType } from '@angular/common/http';
import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize, Subscription } from 'rxjs';
import { Product } from 'src/app/DTOS/Product';
import { User } from 'src/app/DTOS/User';
import { AuthResponse } from 'src/app/DTOS/AuthResponse';
import { AuthService } from 'src/app/services/auth.service';
import { ManageProductService } from 'src/app/services/manage-product.service';
import { StorageService } from '../../../services/storage.service';
import { Type } from '../../../DTOS/Type';
import { DecimalPipe } from '@angular/common';
import { Tag } from '../../../DTOS/Tag';
import { ProductFile } from 'src/app/DTOS/ProductFile';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';


export const CONSTANTS = {
  'MSG100': 'Tên sản phẩm không được để trống',
  'MSG101': 'Đường dẫn sản phẩm không được để trống',
  'MSG102': 'Phân loại sản phẩm không được để trống',
  'MSG103': 'Giá sản phẩm không được để trống',
  'MSG104': 'Không có tệp nào để download',
}

const baseUrl = "http://localhost:9000/private/manageProduct";
const price = new Intl.NumberFormat('vi-VN',
  {
    style: 'currency', currency: 'VND',
    minimumFractionDigits: 3
  });
@Component({
  selector: 'app-new-product',
  templateUrl: './new-product.component.html',
  styleUrls: ['./new-product.component.css']
})
export class NewProductComponent implements OnInit {
  readonly CONSTANTS = CONSTANTS;

  constructor(private ElByClassName: ElementRef,
    private storageService: StorageService,
    private formBuilder: FormBuilder,
    private manageProductService: ManageProductService,
    private activatedRoute: ActivatedRoute,
    private decimalPipe: DecimalPipe,
    private router: Router) { }

  uploadProgress: number = 0;

  authResponse: AuthResponse = new AuthResponse();
  product: Product = new Product;
  typeList: Type[] = [];
  tagList: Tag[] = [];

  formattedAmount: string | null | undefined;

  productUrl = "";
  price = '';
  instruction = "";
  productDetails = '';

  public detailsEditor = ClassicEditor;
  newProductForm = this.formBuilder.group({
    "id": [this.product.id, [Validators.required]],
    "name": ['', [Validators.required]],
    url: new FormControl('', [Validators.required]),
    details: [''],
    "description": [''],
    "type": [this.product.type],
    "tags": [this.product.tags],
    price: new FormControl('', [Validators.required, Validators.min(1000), Validators.max(10000000)])
  });

  ngOnInit(): void {
    this.authResponse = this.storageService.getAuthResponse();
    this.getCurrentProduct();
    this.getTypeList();
    this.newProductForm.valueChanges.subscribe((form) => {
      /*const formattedValue = this.getFormattedValue(form.priceUnformated);
      this.newProductForm.patchValue({ priceUnformated: formattedValue }, { emitEvent: false });*/
    });

  }

  onFileUpload($event: any) {
    this.uploadFileIndex($event.target.files.length - 1, $event.target.files);
  }

  uploadFileIndex(index: number, files: File[]) {
    const file: File = files[index];
    if (file) {
      const formData = new FormData();
      formData.set("enctype", "multipart/form-data");
      formData.append("productFile", file);
      formData.append("productId", this.product.id.toString());

      const upload$ = this.manageProductService.uploadProductFile(formData).subscribe(
        (data: Product) => {
          this.uploadProgress = 0;
          this.product = data;
          console.log(this.product);
          if (index >= 1) {
            this.uploadFileIndex(index - 1, files);
          }
        },
        (error) => {
          console.log(error);
        }
      )
    }
  }

  formatFileSize(fileSize: number) {
    if (fileSize < 1000000) {
      return fileSize / 1000 + 'kb';
    } else {
      return (fileSize / 1000000).toFixed(3) + 'Mb';
    }
  }

  deleteFile(file: ProductFile) {
    if (file) {
      const formData = new FormData();
      formData.append("fileId", file.id.toString());
      formData.append("productId", this.product.id.toString());
      const upload$ = this.manageProductService.deleteProductFile(formData).subscribe(
        (data: Product) => {
          this.product = data;
          console.log(data);
        },
        (error) => {
          console.log(error);
        }
      )
    }
  }

  swapElements(index1: number, index2: number) {
    let temp = this.product.files[index1];
    this.product.files[index1] = this.product.files[index2];
    this.product.files[index2] = temp;
  }

  moveFileUp(file: ProductFile) {
    var index = this.product.files.indexOf(file);
    if (index >= 1) {
      this.swapElements(index, index - 1);
    }
  }

  moveFileDown(file: ProductFile) {
    var index = this.product.files.indexOf(file);
    if (index <= this.product.files.length - 2) {
      this.swapElements(index, index + 1);
    }
  }

  getCurrentProduct(): void {
    var productId = this.activatedRoute.snapshot.paramMap.get('productId');
    if (productId) {
      this.manageProductService.getProductByIdAndUser(+productId).subscribe(
        (data) => {
          this.product = data;
          if (this.product.coverImage != "" && this.product.coverImage != null) {
            this.loadCoverImage();
          }
          this.getTagList();
          this.productDetails = this.product.details;     
        },
        (error) => {
          this.router.navigate(['error']);
        }
      );
    }
  }

  getTypeList(): void {
    this.manageProductService.getTypeList().subscribe(
      (data: Type[]) => {
        this.typeList = data;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  getTagList(): void {
    this.manageProductService.getTagList().subscribe(
      (data: Tag[]) => {
        this.tagList = data;
        for (let j = 0; j < this.product.tags.length; j++) {
          for (let i = 0; i < this.tagList.length; i++) {
            if (this.tagList[i].id == this.product.tags[j].id) {
              this.tagList.splice(i, 1);
            }
          }
        }
      },
      (error) => {
        console.log(error);
      }
    );
  }

  loadCoverImage(): void {
    if (!this.coverImg) {
      const img = document.createElement('img');
      img.className = "cover_img";
      img.id = "cover_img";
      this.coverImgContainer?.appendChild(img);
    }
    this.manageProductService.getCoverImage(this.product.id).subscribe(
      (data) => {
        if (this.coverImg) {
          this.coverImg.setAttribute('src', URL.createObjectURL(data));
          if (this.coverImg != null) {
            this.coverImg.style.width = "100%";
            this.coverImg.style.height = "300px";
            this.coverImg.style.padding = "0";
            this.coverImg.style.margin = "0";
          }
        }
        if (this.UploadBtn) {
          this.UploadBtn.className = this.UploadBtn.className.substring(0, this.UploadBtn.className.length - 9);
          this.UploadBtn.className = this.UploadBtn.className.concat(' has_upload');
        }
      },
      (error) => {
        console.log(error);
      }
    );
  }

  onCoverImageUpload($event: any) {
    const file: File = $event.target.files[0];

    if (file) {
      //this.fileName = file.name;
      const formData = new FormData();
      formData.set("enctype", "multipart/form-data");
      formData.append("coverImage", file);
      formData.append("productId", this.product.id.toString());

      const upload$ = this.manageProductService.uploadCoverImage(formData).subscribe(
        (data) => {
          this.uploadProgress = 0;
          this.loadCoverImage();
        },
        (error) => {
          console.log(error);
        }
      )
    }
  }

  onTagSelect($event: any) {
    if ($event.target.value != 0) {
      for (let i = 0; i < this.tagList.length; i++) {
        if (this.tagList[i].id == $event.target.value) {
          if (!this.product.tags.includes(this.tagList[i])) {
            this.product.tags.push(this.tagList[i]);
            this.tagList.splice(i, 1);
          }
        }
      }
    }
    $event.target.value = 0;
  }

  onRemoveSelectedTag(tagId: number) {
    for (let i = 0; i < this.product.tags.length; i++) {
      if (this.product.tags[i].id == tagId) {
        this.tagList.push(this.product.tags[i]);
        this.product.tags.splice(i, 1);
      }
    }
  }

  isTypeSelected(typeId: number): any {
    if (this.product.type != null) {
      if (typeId === this.product.type.id) {
        this.TypeList.selectedIndex = typeId - 1;
        return "";
      }
    }
    return null;
  }

  onSelectType($event: any): void {
    this.product.type.id = $event.target.value;
    for (let i = 0; i < this.typeList.length; i++) {
      if (this.typeList[i].id == $event.target.value) {
        this.product.type = this.typeList[i];
        break;
      }
    }
  }

  onUrlInput($event: any): void {
    const keyCode = $event.keyCode;
    console.log(keyCode);
    const notAllowedKey = [32, 192, 189, 191, 16];
    if (notAllowedKey.includes(keyCode) || ($event.shiftKey === true && keyCode >= 48 && keyCode <= 57) || ($event.ctrlKey === true && keyCode==86)) {
      $event.preventDefault();
    }
  }

  onPriceChange($event: any): void {
    const keyCode = $event.keyCode;
    var minPrice = 1;
    const excludedKeys = [8, 37, 39, 46];
    if (!((keyCode >= 48 && keyCode <= 57) ||
      (keyCode >= 96 && keyCode <= 105) ||
      (excludedKeys.includes(keyCode)))) {
      $event.preventDefault();
    }
  }

  getFormattedValue(value: any): string {
    const stringToTransform = String(value ?? '')
      .replace(/\D/g, '')
      .replace(/^0+/, '');
    return (
      this.decimalPipe.transform(stringToTransform === '' ? '0' : stringToTransform, '1.0')
      + 'đ');
  }

  onChoosePricingOption($event: { target: any; srcElement: any; }) {
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
       // this.newProductForm.value.priceUnformated = '0';
        if (paid != null) {
          paid.setAttribute("style", "display:none; ");
        }
        if (noPayment != null) {
          noPayment.setAttribute("style", "display: block;");
        }
      }
    }
  }

  saveProduct() {
    this.newProductForm.controls.id.setValue(this.product.id);
    this.newProductForm.controls.tags.setValue(this.product.tags);
    this.newProductForm.controls.type.setValue(this.product.type);
    this.newProductForm.controls.url.setValue(this.productUrl);
    this.newProductForm.controls.details.setValue(this.productDetails);
    console.log(this.newProductForm);
    this.manageProductService.updateProduct(this.newProductForm.value, this.InstructionDetails.value).subscribe(
      (data) => {
        this.product = data;
        console.log(data);
      },
      (error) => {
        console.log(error);
      }
    )
  }

  get DefaultTagSelectOption() {
    return document.getElementById('default_tag') as HTMLOptionElement;
  }

  get TagList() {
    return document.getElementById('tag_list') as HTMLSelectElement;
  }

  get TypeList() {
    return document.getElementById('type_list') as HTMLSelectElement;
  }

  get UploadBtn() {
    return document.getElementsByClassName('upload_buttons').item(0);
  }

  get coverImg() {
    return document.getElementById('cover_img');
  }

  get coverImgContainer() {
    return document.getElementById('cover_img_div');
  }

  get Price() {
    return document.getElementById('price') as HTMLInputElement;
  }

  get TextEditor() {
    return document.getElementsByClassName('ck - editor__editable_inline');
  }

  get InstructionDetails() {
    return document.getElementById('instruction_details') as HTMLTextAreaElement;
  }
}
