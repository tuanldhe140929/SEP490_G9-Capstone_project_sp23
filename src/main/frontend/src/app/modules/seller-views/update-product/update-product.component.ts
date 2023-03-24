import { HttpClient, HttpEventType, HttpResponse } from '@angular/common/http';
import { AfterViewInit, Component, ElementRef, Input, OnInit, Renderer2, TemplateRef, ViewChild } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize, Subscription } from 'rxjs';
import { Product } from 'src/app/DTOS/Product';
import { User } from 'src/app/DTOS/User';
import { AuthResponse } from 'src/app/DTOS/AuthResponse';
import { ManageProductService } from 'src/app/services/manage-product.service';
import { StorageService } from '../../../services/storage.service';
import { Category } from '../../../DTOS/Category';
import { DecimalPipe } from '@angular/common';
import { Tag } from '../../../DTOS/Tag';
import { FileState, ProductFile } from 'src/app/DTOS/ProductFile';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Preview } from '../../../DTOS/Preview';
import { PreviewService } from '../../../services/preview.service';
import { ProductFileService } from '../../../services/product-file.service';
import { License } from 'src/app/DTOS/License';
import { CategoryService } from 'src/app/services/category.service';
import { TagService } from 'src/app/services/tag.service';
import { ProductService } from '../../../services/product.service';


const MSG100 = 'Tên sản phẩm không được để trống';
const MSG1001 = 'Tên sản phẩm có độ dài từ 3 đến 30 kí tự'
const MSG1002 = 'Độ dài tối đa của mô tả là 100';
const MSG1003 = 'Độ dài tối đa của mô tả chi tiết là 1000 kí tự'
const MSG1004 = 'Độ dài tối đa của hướng dẫn là 200 kí tự'
const MSG1005 = 'Mỗi sản phẩm chỉ có tối đa 5 nhãn'
const MSG102 = 'Phân loại sản phẩm không được để trống';
const MSG103 = 'Giá sản phẩm không được để trống';
const MSG1031 = 'Giá sản phẩm tối đa là 50.000.000';
const MSG104 = 'Không có tệp nào để download';
const MSG105 = 'Định dạng này không được hỗ trợ';

const IMAGE_EXTENSIONS = ['image/png', 'image/jpeg', 'image/svg+xml'];
const VIDEO_EXTENSIONS = ['video/mp4', 'video/x-matroska', 'video/quicktime'];
const baseUrl = "http://localhost:9000/private/manageProduct";
const MAX_SIZE = 50000000000;
const MAX_FILE_COUNT = 10;
const CHUNK_SIZE = 50000000;

class UploadProcess {
  progress: number;
  subcription: Subscription

  constructor() {
    this.progress = 0;
    this.subcription = new Subscription;
  }
}

class FileDisplay {
  file: ProductFile;
  process: UploadProcess;
  constructor() {
    this.file = new ProductFile;
    this.process = new UploadProcess;
  }
}
@Component({
  selector: 'app-update-product',
  templateUrl: './update-product.component.html',
  styleUrls: ['./update-product.component.css']
})
export class UpdateProductComponent implements OnInit {

  @ViewChild('content', { static: false }) private content: any;

  @ViewChild('notAcceptFormatModal', { static: false }) private notAcceptFormatModal: any;

  @ViewChild('fileSizeErrorModal', { static: false }) private fileSizeErrorModal: any;

  @ViewChild('specifyVersionModal', { static: false }) private specifyVersionModal: any;

  @ViewChild('infoModal', { static: false }) private infoModal: any;


  fileError = "";
  constructor(private ElByClassName: ElementRef,
    private storageService: StorageService,
    private formBuilder: FormBuilder,
    private manageProductService: ManageProductService,
    private activatedRoute: ActivatedRoute,
    private decimalPipe: DecimalPipe,
    private router: Router,
    private modalService: NgbModal,
    private previewService: PreviewService,
    private productFileService: ProductFileService,
    private categoryService: CategoryService,
    private tagService: TagService,
    private productService: ProductService) { }

  product: Product = new Product;
  typeList: Category[] = [];
  tagList: Tag[] = [];
  licenseList: License[] = [];
  productFileList: ProductFile[] = [];
  fileDisplayList: FileDisplay[] = [];
  formattedAmount: string | null | undefined;


  info = '';
  productUrl = "";
  productVersions: Product[] = [];
  versions: string[] = [];
  price = 0;
  private lastValue: string = '';

  instruction = "";
  productDetails = '';
  draft = true;
  productCate = -1;
  productLicense = -1;
  assertDate = new Date(0);
  public detailsEditor = ClassicEditor;
  newProductForm = this.formBuilder.group({
    "id": [this.product.id, [Validators.required]],
    "name": ['', [Validators.required]],
    details: [''],
    "version": [''],
    "description": [''],
    "license": [this.product.license],
    "category": [this.product.category],
    "tags": [this.product.tags],
    "draft": [true, Validators.required],
    price: new FormControl('')
  });

  ngOnInit(): void {
    this.getCurrentProduct();
    this.getTypeList();
    this.getLicenseList();


  }
  getLicenseList() {
    this.productService.getAllLicense().subscribe((data) => {
      this.licenseList = data;
    }, (error) => {

    })
  }



  onFileUpload($event: any) {
    var totalSize = 0;
    for (let i = 0; i < $event.target.files.length; i++) {

      if ($event.target.files[i].size == 0) {
        this.fileError = "File đăng tải có kích thước: 0Kb";
        this.openFileSizeErrorModal();
        return;
      }

      for (let j = 0; j < this.fileDisplayList.length; j++) {
        if (this.fileDisplayList[j].file.name === $event.target.files[i].name) {
          this.fileError = "Tên File đã tồn tại";
          this.openFileSizeErrorModal();
          return;
        }
        totalSize += this.fileDisplayList[j].file.size;
      }
      totalSize += $event.target.files[i].size;
      if (totalSize > MAX_SIZE) {
        this.fileError = "Tổng dung lượng các file vượt quá 5GB";
        this.openFileSizeErrorModal();
        return;
      }

      if (this.fileDisplayList.length + 1 > MAX_FILE_COUNT) {
        this.fileError = "Vượt quá tối đa" + MAX_FILE_COUNT + " file";
        this.openFileSizeErrorModal();
        return;
      }

      var productFile = ProductFile.fromFile($event.target.files[i]);
      var fileDisplay = new FileDisplay();
      fileDisplay.file = productFile;
      fileDisplay.file.fileState = FileState.ON_QUEUE;
      this.fileDisplayList.push(fileDisplay);
    }

    this.uploadFileIndex($event.target.files.length - 1, $event.target.files);
  }

  uploadFileIndex(index: number, files: File[]) {
    const file: File = files[index];
    if (file) {
      let fileDisplay: FileDisplay = new FileDisplay;

      for (let i = 0; i < this.fileDisplayList.length; i++) {
        if (this.fileDisplayList[i].file.name == file.name) {
          fileDisplay = this.fileDisplayList[i];
        }
      }

      const formData = new FormData();
      formData.set("enctype", "multipart/form-data");
      formData.append("productFile", file);
      formData.append("productId", this.product.id.toString());
      formData.append("version", this.product.version);

      if (fileDisplay.file.name != '' && fileDisplay.file.name != null) {
        const upload$ = this.productFileService.uploadProductFile(formData).subscribe(
          (event) => {
            if (event.type === HttpEventType.UploadProgress) {
              fileDisplay.process.progress = Math.round(100 * event.loaded / file.size);
              fileDisplay.file.fileState = FileState.UPLOADING;
              if (fileDisplay.process.progress >= 100) {
                fileDisplay.file.fileState = FileState.SCANNING;
              }
            } else if (event instanceof HttpResponse) {
              var ret: ProductFile = event.body;
              fileDisplay.file = ret;
              console.log(ret);
              if (index >= 1) {
                this.uploadFileIndex(index - 1, files);
              }
            }
          },
          (error) => {
            fileDisplay.file.fileState = FileState.ERROR;
            this.info = "Không thể tải lên file " + file.name;
            this.openInfoModal();
            var index = -1;
            for (let i = 0; i < this.fileDisplayList.length; i++) {
              if (file.name == this.fileDisplayList[i].file.name) {
                index = i;
                break;
              }
            }
            if (index != -1) {
              this.fileDisplayList.slice(index, 1);
            }
          }
        )
        fileDisplay.process.subcription = upload$;
      }
    }
  }









  cancelUpload(file: FileDisplay) {
    file.process.subcription.unsubscribe();
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
      formData.append("version", this.product.version);
      const upload$ = this.productFileService.deleteProductFile(formData).subscribe(
        (data: ProductFile) => {
          var index = -1;
          for (let i = 0; i < this.fileDisplayList.length; i++) {
            if (this.fileDisplayList[i].file.id === data.id) {
              index = i;
            }
          }
          if (index != -1) {
            this.fileDisplayList.splice(index, 1);
            console.log(this.fileDisplayList.length);
          }

          console.log(data.lastFile && !this.product.draft);
          console.log(data.lastFile);
          console.log(this.product.draft);
          if (data.lastFile && !this.product.draft) {
            this.info = 'Vì không có downloadable file nào, trạng thái sản phẩm sẽ chuyển về "Nháp"';
            this.openInfoModal();
            this.Draft.checked = true;
            this.Publish.checked = false;
          }
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

  getCurrentProduct(): void {
    var productId = this.activatedRoute.snapshot.paramMap.get('productId');
    if (productId) {
      var version = this.activatedRoute.snapshot.paramMap.get('version');
      if (version != null) {
        this.getByProductIdAndVersion(productId, version);
      } else {
        this.getActiveVersion(productId);
      }

      this.getAllVersionOfProduct(+productId);
    }
  }

  getAllVersionOfProduct(productId: number) {
    const request = this.manageProductService.getAllVersionOfProduct(productId);
    request.subscribe(data => {
      this.productVersions = data;
      for (let i = 0; i < this.productVersions.length; i++) {
        this.versions.push(this.productVersions[i].version);
      }
      console.log(this.versions);
    })
  }

  getByProductIdAndVersion(productId: string | null, version: string) {
    if (productId && version) {
      this.productService.getProductByIdAndVersion(+productId, version).subscribe(
        (data) => {

          this.product = data;
          console.log(this.product);
          this.getTagList();
          this.productDetails = this.product.details;
          this.draft = this.product.draft;

          if (this.product.category != null) {
            this.productCate = this.product.category.id
          } else {
            this.productCate = -1;
          }

          if (this.product.draft) {
            this.Draft.checked = true;
          } else {
            this.Publish.checked = true;
          }

          if (this.product.license != null) {
            this.productLicense = this.product.license.id;
            console.log(this.productLicense);
          } else {
            this.productLicense = -1;
          }

          this.InstructionDetails.value = this.product.instruction;

          for (let i = 0; i < this.Columns.length; i++) {
            this.Columns[i].setAttribute('style', 'width: ' + 100 / this.Columns.length + '; height: 100 %;background - color: black; opacity: 0.6;');
          }

          if (this.product.coverImage != '' && this.product.coverImage != null) {
            this.loadCoverImage();
          }

          if (this.product.price != 0) {
            this.PayBtn.className += ' active';
            this.formattedPrice = this.getFormattedValue(this.product.price);
          } else {
            console.log("no payment");
            this.NoPaymentBtn.className += ' active';
            this.price = 0;
            const paid = (<HTMLElement>this.ElByClassName.nativeElement).querySelector(
              '.paid'
            );
            if (paid != null) {
              paid.setAttribute("style", "display:none;");
            }

            const noPayment = (<HTMLElement>this.ElByClassName.nativeElement).querySelector(
              '.no_payment'
            );

            if (noPayment) {
              noPayment.setAttribute("style", "display:block;");
            }
          }

          this.percent = 'width:' + 100 / this.product.previewPictures.length + '%;';

          this.product.files
          for (let i = 0; i < this.product.files.length; i++) {
            var productFile: ProductFile = this.product.files[i];
            var fileDisplay = new FileDisplay();
            fileDisplay.file = productFile;
            fileDisplay.file.fileState = FileState.STORED;
            this.fileDisplayList.push(fileDisplay);
          }
          console.log(this.fileDisplayList);
        },

        (error) => {
          console.log(error);
        }
      );
    }
  }
  getActiveVersion(productId: string | null) {
    if (productId) {
      this.manageProductService.getActiveVersionProductByIdAndSeller(+productId).subscribe(
        (data) => {

          this.product = data;
          console.log(this.product);
          this.getTagList();
          this.productDetails = this.product.details;
          this.draft = this.product.draft;

          if (this.product.category != null) {
            this.productCate = this.product.category.id
          } else {
            this.productCate = -1;
          }

          if (this.product.draft) {
            this.Draft.checked = true;
          } else {
            this.Publish.checked = true;
          }
          if (this.product.license != null) {
            this.productLicense = this.product.license.id;
          } else {
            this.productLicense = -1;
          }

          this.InstructionDetails.value = this.product.instruction;

          for (let i = 0; i < this.Columns.length; i++) {
            this.Columns[i].setAttribute('style', 'width: ' + 100 / this.Columns.length + '; height: 100 %;background - color: black; opacity: 0.6;');
          }

          if (this.product.coverImage != '' && this.product.coverImage != null) {
            this.loadCoverImage();
          }

          this.percent = 'width:' + 100 / this.product.previewPictures.length + '%;';

          this.product.files
          for (let i = 0; i < this.product.files.length; i++) {
            var productFile: ProductFile = this.product.files[i];
            var fileDisplay = new FileDisplay();
            fileDisplay.file = productFile;
            fileDisplay.file.fileState = FileState.STORED;
            this.fileDisplayList.push(fileDisplay);
          }
          console.log(this.fileDisplayList);

          if (this.product.price != 0) {
            this.PayBtn.className += ' active';
            this.formattedPrice = this.getFormattedValue(this.product.price);
          } else {
            console.log("no payment");
            this.NoPaymentBtn.className += ' active';
            this.price = 0;
            const paid = (<HTMLElement>this.ElByClassName.nativeElement).querySelector(
              '.paid'
            );
            if (paid != null) {
              paid.setAttribute("style", "display:none;");
            } const noPayment = (<HTMLElement>this.ElByClassName.nativeElement).querySelector(
              '.no_payment'
            );

            if (noPayment) {
              noPayment.setAttribute("style", "display:block;");
            }

          }
        },

        (error) => {
          console.log(error);
        }
      );
    }
  }





  percent = 'width:100%;';
  getTypeList(): void {
    this.categoryService.getAllCategories().subscribe((response: any) => {
      this.typeList = response;
    })

  }

  getTagList(): void {
    this.tagService.getAllTags().subscribe(
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
    if (this.coverImg != null) {
      this.coverImg.setAttribute('src', 'http://localhost:9000/public/serveMedia/image?source=' + this.product.coverImage.replace(/\\/g, '/'));
      this.coverImg.style.width = "100%";
      this.coverImg.style.height = "100%";
      this.coverImg.style.padding = "0";
      this.coverImg.style.margin = "0";
    }

    if (this.UploadBtn) {
      this.UploadBtn.className = this.UploadBtn.className.substring(0, this.UploadBtn.className.length - 9);
      this.UploadBtn.className = this.UploadBtn.className.concat(' has_upload');
    }

  }

  checkFileType(fileType: string, acceptType: string[]): boolean {
    return acceptType.includes(fileType);
  }

  fileFormatError = MSG105;
  onCoverImageUpload($event: any) {
    const file: File = $event.target.files[0];

    if (file) {
      console.log(file.type);
      if (!this.checkFileType(file.type, IMAGE_EXTENSIONS)) {
        this.openFormatErrorModal();
      } else {
        //this.fileName = file.name;
        const formData = new FormData();
        formData.set("enctype", "multipart/form-data");
        formData.append("coverImage", file);
        formData.append("productId", this.product.id.toString());
        formData.append("version", this.product.version);
        const upload$ = this.manageProductService.uploadCoverImage(formData).subscribe(
          (data: string) => {
			  console.log(this.product);
			  this.CoverImageUploadBtn.value="";
            this.product.coverImage = data;
            console.log(this.product);
            this.loadCoverImage();
          },
          (error: any) => {
			  this.CoverImageUploadBtn.value="";
            this.fileError = 'Tải lên hình ảnh không thành công';
            this.openFileSizeErrorModal();
          }
        )
      }
    }
  }

  getPreviewVideoSource(): string {
    if (this.product.previewVideo.id != -1 && this.product.previewVideo != null) {
      return "http://localhost:9000/public/serveMedia/video?source=" + this.product.previewVideo.source.replace(/\\/g, '/');
    }
    return "";
  }

  onPreviewVideoUpload($event: any) {
    const file: File = $event.target.files[0];
    if (file) {
      if (!this.checkFileType(file.type, VIDEO_EXTENSIONS)) {
        this.openFormatErrorModal();
      } else {
        //this.fileName = file.name;
        const formData = new FormData();
        formData.set("enctype", "multipart/form-data");
        formData.append("previewVideo", file);
        formData.append("productId", this.product.id.toString());
        formData.append("version", this.product.version);
        const upload$ = this.previewService.uploadPreviewVideo(formData).subscribe(
          (data) => {
			  this.PreviewUploadVideoBtn.value="";
            console.log(data);
            this.product.previewVideo = data;
          },
          (error) => {
			  this.PreviewUploadVideoBtn.value="";
            this.fileError = 'Tải lên video không thành công';
            this.openFileSizeErrorModal();
          }
        )
      }
    }
  }

  removePreviewVideo() {
    this.previewService.removePreviewVideo(this.product.id, this.product.version).subscribe(
      (data) => {
        this.product.previewVideo = new Preview;
        console.log(data);
      },
      (error) => {
        console.log(error);
      }
    )
  }

  onPreviewPicturesUpload($event: any) {
    const files: File[] = $event.target.files;
    console.log($event.target.files.length);
    if (files) {
      var valid = true;
      for (let i = 0; i < files.length; i++) {
        if (!this.checkFileType(files[i].type, IMAGE_EXTENSIONS)) {
          valid = false;
        }
      }
      if (!valid) {
        this.openFormatErrorModal();
      } else {
        for (let i = 0; i < files.length; i++) {
          const formData = new FormData();
          formData.set("enctype", "multipart/form-data");
          formData.append("previewPicture", files[i]);
          formData.append("productId", this.product.id.toString());
          formData.append("version", this.product.version);
          const upload$ = this.previewService.uploadPreviewPicture(formData).subscribe(
            (data) => {
              console.log(data);
              this.PreviewUploadImageBtn.value="";
              this.product.previewPictures = data;
              this.percent = 'width:' + 100 / this.product.previewPictures.length + '%;';
            },
            (error) => {
				this.PreviewUploadImageBtn.value="";
              this.fileError = 'Tải lên hình ảnh không thành công';
              this.openFileSizeErrorModal();
            }
          )
        }
      }
    }
  }

  chooseVersion(ver: Product) {
    window.location.href = "http://localhost:4200/product/update/" + this.product.id + "/" + ver.version;
  }

  getPreviewPictureSource(preview: Preview): string {
    return "http://localhost:9000/public/serveMedia/image?source=" + encodeURIComponent(preview.source.replace(/\\/g, '/'));
  }

  onChooseImage(index: number) {
    for (let i = 0; i < this.Slides.length; i++) {
      this.Slides[i].setAttribute('style', this.Slides[i].getAttribute('style') + ' display: none;');

      this.Columns[i].className = this.Columns[i].className.replace(" active", "");

    }
    this.Slides[index].setAttribute('style', this.Slides[index].getAttribute('style') + ' display: block;');
    this.Columns[index].className += " active";

  }

  removePreviewPicture(preview: Preview) {
    if (this.RemovePreviewPictureBtn) {
      this.RemovePreviewPictureBtn.disabled = true;
    }
    this.previewService.removePreviewPicture(preview.id).subscribe(
      (data) => {
        this.product.previewPictures = data;
        this.percent = 'width:' + 100 / this.product.previewPictures.length + '%;';
        if (this.RemovePreviewPictureBtn) {
          this.RemovePreviewPictureBtn.disabled = false;
        }
      },
      (error) => {
      })
  }

  onDraftSelect($event: any) {
    if ($event.target.value == 'true') {
      this.draft = true;
      this.Draft.checked = true;
      this.Publish.checked = false;
    } else {
      this.draft = false;
      this.Draft.checked = false;
      this.Publish.checked = true;
    }
  }

  onTagSelect($event: any) {
    if ($event.target.value != 0) {
      if (this.product.tags.length < 5) {
        for (let i = 0; i < this.tagList.length; i++) {
          if (this.tagList[i].id == $event.target.value) {
            if (!this.product.tags.includes(this.tagList[i])) {
              this.product.tags.push(this.tagList[i]);
              this.tagList.splice(i, 1);
            }
          }
        }
      }
    } else {
      this.fileError = MSG1005;
      this.openFileSizeErrorModal();
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
    if (this.productCate != -1) {
      if (typeId === this.productCate) {
        this.TypeList.selectedIndex = typeId;
        return "";
      }
    }
    return null;
  }

  isLicenseSelected(licenseId: number): any {
    if (this.productLicense != -1) {
      if (licenseId === this.productLicense) {
        this.LicenseList.selectedIndex = licenseId;
        return "";
      }
    }
    return null;
  }

  onSelectType($event: any): void {
    this.product.category.id = $event.target.value;
    for (let i = 0; i < this.typeList.length; i++) {
      if (this.typeList[i].id == $event.target.value) {
        this.product.category = this.typeList[i];
        break;
      }
    }
  }

  onUrlInput($event: any): void {
    const keyCode = $event.keyCode;
    console.log(keyCode);
    const notAllowedKey = [32, 192, 189, 191, 16];
    if (notAllowedKey.includes(keyCode) || ($event.shiftKey === true && keyCode >= 48 && keyCode <= 57) || ($event.ctrlKey === true && keyCode == 86)) {
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
      + '');
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

  errors: string[] = [];

  dismissError() {
    this.errors = [];
    this.modalService.dismissAll();
  }

  saveProduct() {
    this.newProductForm.markAllAsTouched;
    this.newProductForm.markAsDirty;

    if (this.ProductName.value == null || this.ProductName.value.trim() == '') {
      this.errors.push(MSG100);
    }
    if (this.ProductName.value.length > 30 || this.ProductName.value.length < 3) {
      this.errors.push(MSG1001);
    }

    if (this.newProductForm.value.price == null) {
      this.errors.push(MSG103);
    }
    if (this.fileDisplayList.length == 0) {
      this.errors.push(MSG104);
    }
    if (this.product.description != null)
      if (this.product.description.length > 100) {
        const MSG1002 = 'Độ dài tối đa của mô tả là 100';
        this.errors.push();
      }

    if (this.productDetails != null)
      if (this.productDetails.length > 1000) {
        this.errors.push(MSG1003);
      }

    if (this.product.price > 50000000) {
      this.errors.push()
    }

    if (this.InstructionDetails.value.length > 200) {
      this.errors.push(MSG1004);
    }
    if (this.productCate == -1 || this.productCate == 0) {
      this.errors.push(MSG102);
    }

    if (this.Paid.style.display === 'none') {
      this.price = 0;
      console.log(true);
    } else {

      var priceString = this.formattedPrice.replace(",", "");
      this.price = Number.parseInt(priceString);
      console.log(priceString);
      console.log(Number.parseInt(priceString));
    }

    if (this.price > 50000000) {
      this.errors.push("Số tiền tối đa là 50.000.000đ");
    }

    if (this.price % 1000 != 0) {
      this.errors.push('Số tiền là bội số của 1000');
    }



    if (this.errors.length == 0) {

      this.newProductForm.controls.id.setValue(this.product.id);
      this.newProductForm.controls.name.setValue(this.product.name);
      this.newProductForm.controls.tags.setValue(this.product.tags);
      this.newProductForm.controls.category.setValue(this.typeList[this.productCate - 1]);
      this.newProductForm.controls.license.setValue(this.licenseList[this.productLicense - 1])
      this.newProductForm.controls.details.setValue(this.productDetails);
      this.newProductForm.controls.draft.setValue(this.draft);
      this.newProductForm.controls.price.setValue(this.price.toString());
      this.newProductForm.controls.description.setValue(this.product.description);
      this.newProductForm.controls.version.setValue(this.product.version);


      this.manageProductService.updateProduct(this.newProductForm.value, this.InstructionDetails.value).subscribe(
        (data) => {
          this.product = data;
          console.log(this.product);
          this.info = "Cập nhật thành công";
          this.openInfoModal();
        },
        (error) => {
          this.fileError = "Cập nhật không thành công";
          this.openFileSizeErrorModal();
          console.log(error);
        }
      )
    } else { this.openVerticallyCentered(); }
  }

  createNewVersion() {
    var newVersion = this.NewVersionSpecify.value;
    if (newVersion.endsWith(".")) {
      this.fileError = 'Version cannot end with "."';
      this.openFileSizeErrorModal();
    }
    if (this.versions.includes(newVersion)) {
      this.fileError = 'Version exist!';
      this.openFileSizeErrorModal();
    }
    else {
      this.productService.createNewVersion(this.product, newVersion).subscribe(
        data => {
          console.log(data);
          window.location.href = 'http://localhost:4200/product/update/'+this.product.id+'/' + newVersion;
        },
        error => {
          console.log(error);
        });
    }
  }

  get PayBtn() {
    return document.getElementById('pay_btn') as HTMLButtonElement;
  }

  get NoPaymentBtn() {
    return document.getElementById('no_payment_btn') as HTMLButtonElement;
  }

  get Paid() {
    return document.getElementById('paid') as HTMLDivElement;
  }

  get No_Payment() {
    return document.getElementById('no_payment') as HTMLDivElement;
  }

  get LicenseDetails() {
    if (this.productLicense >= 1 && this.licenseList.length > 0) {
      return this.licenseList[this.productLicense - 1].details;
    }
    else {
      return "";
    }
  }

  get LicenseReferenceLink() {
    if (this.productLicense >= 1) {
      if (this.licenseList[this.productLicense - 1] != null) {
        return this.licenseList[this.productLicense - 1].referenceLink;
      }
      else
        return "";
    }
    else {
      return "";
    }
  }
  get NewVersionSpecify() {
    return document.getElementById('new_version') as HTMLInputElement;
  }

  openSpecifyVersionModal() {
    this.modalService.open(this.specifyVersionModal, { centered: true });
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

  get LicenseList() {
    return document.getElementById('license_list') as HTMLSelectElement;
  }

  get UploadBtn() {
    return document.getElementsByClassName('upload_buttons').item(0);
  }
  get ProductUrl() {
    return document.getElementById('productUrl') as HTMLInputElement;
  }

  get ProductName() {
    return document.getElementById('product_name') as HTMLInputElement;
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

  openVerticallyCentered() {
    this.modalService.open(this.content, { centered: true });
  }

  openFormatErrorModal() {
    this.modalService.open(this.notAcceptFormatModal, { centered: true });
  }

  openFileSizeErrorModal() {
    this.modalService.open(this.fileSizeErrorModal, { centered: true });
  }

  openInfoModal() {
    this.modalService.open(this.infoModal, { centered: true });
  }

  get Draft() {
    return document.getElementById('draft') as HTMLInputElement;
  }
  get Publish() {
    return document.getElementById('publish') as HTMLInputElement;
  }

  get PreviewVideo() {
    return document.getElementById('preview_video') as HTMLVideoElement;
  }

  get Slides() {
    return document.querySelectorAll('.mySlides');
  }
  get Columns() {
    return document.querySelectorAll('.column');
  }

  get RemovePreviewPictureBtn() {
    return document.getElementById('remove_picture_btn') as HTMLButtonElement;
  }
  
  get PreviewUploadImageBtn(){
	  return document.getElementById('previewUploadImage') as HTMLInputElement;
  }
  
  get PreviewUploadVideoBtn(){
	   return document.getElementById('previewUploadVideo') as HTMLInputElement;
  }
  
  get CoverImageUploadBtn(){
	   return document.getElementById('coverImageUpload') as HTMLInputElement;
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

  public get FileState(): typeof FileState {
    return FileState;
  }


  formattedPrice = '10,000';

  updatePrice(value: any) {
    this.priceError();
    this.formattedPrice = this.getFormattedValue(value);
  }


  exclusiveKey = [13, 32];
  exclusiveKey2 = [8, 32, 190]


  checkInput($event: any) {
    if (this.exclusiveKey.includes($event.keyCode) && $event.keyCode < 48 || $event.keyCode > 57) {
      $event.preventDefault();
    }
    this.priceErrorr = [];
    console.log($event.keyCode);
  }

  version = '';
  versionError: string[] = [];
  checkInputVersion($event: any) {
    if (!this.exclusiveKey2.includes($event.keyCode) && $event.keyCode < 48 && ($event.keyCode > 57 && $event.keyCode < 65) && $event.keyCode > 90) {
      $event.preventDefault();
    }
    let maxLength = 6;
    let a = 's';

    let value = $event.target.value;
    console.log($event.keyCode);
    if (value.length + 1 > maxLength && $event.keyCode != 8) {
      $event.preventDefault();
    } this.versionError = [];

  }

  priceErrorr: string[] = [];

  priceError() {
    let intValue = Number.parseInt(this.formattedPrice.replace(',', ""));

    if (intValue < 10000) {
      this.priceErrorr.push("Số tiền tối thiểu là 10.000đ")
    } else {

      if (intValue % 1000 != 0) {
        this.priceErrorr.push("Tiền là bội số của 1.000");
      }
    }
    console.log(this.priceErrorr);
  }

  isCurrent(version: Product): boolean {
    console.log(version.activeVersion);;
    if (version.version == this.product.activeVersion)
      return true;
    else
      return false;
  }

  activeVersion(version: Product) {
    const $request = this.manageProductService.activeVersion(version);
    $request.subscribe(
      (data) => {
       
        if(data==true)
          this.product.activeVersion = version.version;
          else{
			     this.fileError = "Phiên bản mục tiêu không có tệp nào để download, không thể sử dụng phiên bản này";
        this.openFileSizeErrorModal();
		  }
      },
      (error) => {
        this.fileError = "Thay đổi phiên bản sản phẩm không thành công";
        this.openFileSizeErrorModal();
      });
  }
}
