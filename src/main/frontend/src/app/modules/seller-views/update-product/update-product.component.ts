import { HttpClient, HttpEventType, HttpResponse } from '@angular/common/http';
import { AfterViewInit, ChangeDetectorRef, Component, ElementRef, Input, OnInit, Renderer2, TemplateRef, ViewChild } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize, Subscription } from 'rxjs';
import { Product, Status } from 'src/app/dtos/Product';
import { ManageProductService } from 'src/app/services/manage-product.service';
import { StorageService } from '../../../services/storage.service';
import { Category } from '../../../dtos/Category';
import { DecimalPipe } from '@angular/common';
import { Tag } from '../../../dtos/Tag';
import { FileState, ProductFile } from 'src/app/dtos/ProductFile';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Preview } from '../../../dtos/Preview';
import { PreviewService } from '../../../services/preview.service';
import { ProductFileService } from '../../../services/product-file.service';
import { License } from 'src/app/dtos/License';
import { CategoryService } from 'src/app/services/category.service';
import { TagService } from 'src/app/services/tag.service';
import { ProductService } from '../../../services/product.service';
import { MatDialog } from '@angular/material/dialog';
import { ApplyLicenseComponent } from './apply-license/apply-license.component';

const MSG100 = 'Tên sản phẩm không được để trống';
const MSG1001 = 'Tên sản phẩm có độ dài từ 3 đến 30 kí tự'
const MSG1002 = 'Độ dài tối đa của mô tả là 100';
const MSG1003 = 'Độ dài tối đa của mô tả chi tiết là 1000 kí tự'
const MSG1004 = 'Độ dài tối đa của hướng dẫn là 200 kí tự'
const MSG1005 = 'Mỗi sản phẩm chỉ có tối đa 5 nhãn'
const MSG102 = 'Phân loại sản phẩm không được để trống';
const MSG103 = 'Giá sản phẩm không được để trống';
const MSG1031 = 'Giá sản phẩm tối đa là 1000$';
const MSG104 = 'Không có tệp nào để download';
const MSG105 = 'Định dạng này không được hỗ trợ';

const IMAGE_EXTENSIONS = ['image/png', 'image/jpeg', 'image/svg+xml'];
const VIDEO_EXTENSIONS = ['video/mp4', 'video/x-matroska', 'video/quicktime'];
const baseUrl = "http://localhost:9000/private/manageProduct";
const MAX_SIZE = 1024 * 1024 * 2000;
const MAX_FILE_COUNT = 10;
const CHUNK_SIZE = 50000000;
const MAX_FILE_SIZE = 1024 * 1024 * 500;
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
    private productService: ProductService,
    private cd: ChangeDetectorRef,
    private dialog: MatDialog) { }

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
    "category": [this.product.category],
    "tags": [this.product.tags],
    price: new FormControl('')
  });

  ngOnInit(): void {
    this.getCurrentProduct();
    this.getTypeList();
    this.getLicenseList();


  }


  public editorConfig = {
    toolbar: {
      items: [
        'heading',
        '|',
        'italic',
        'bold',
        'bulletedList',
        'numberedList',
        'removeFormat',

      ]
    }
  };

  getLicenseList() {
    this.productService.getAllLicense().subscribe((data) => {
      this.licenseList = data;
    }, (error) => {

    })
  }



  onFileUpload($event: any) {
    if (this.product.approved != Status.NEW) {
      this.fileError = "Bạn cần click vào nút 'Tiếp tục cập nhật' để thực hiện hành động này";
      this.openFileSizeErrorModal();
      return;
    }
    var totalSize = 0;
    if (this.fileDisplayList.length + $event.target.files.length > MAX_FILE_COUNT) {
      this.fileError = "Vượt quá tối đa" + MAX_FILE_COUNT + " file";
      this.openFileSizeErrorModal();
    } else {
      for (let i = 0; i < $event.target.files.length; i++) {

        if ($event.target.files[i].size == 0) {
          this.fileError = "File đăng tải có kích thước: 0Kb";
          this.openFileSizeErrorModal();
          return;
        }

        if ($event.target.files[i].size > MAX_FILE_SIZE) {
          this.fileError = "File đăng tải có kích thước: tối đa 500Mb";
          this.openFileSizeErrorModal();
          return;
        }

        if ($event.target.files[i].name.length > 100 || 0 > $event.target.files[i].name.length) {
          this.fileError = "Tên tệp từ 1 đến 100 kí tự";
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
          this.fileError = "Tổng dung lượng các file vượt quá 2GB";
          this.openFileSizeErrorModal();
          return;
        }

        var productFile = ProductFile.fromFile($event.target.files[i]);
        var fileDisplay = new FileDisplay();
        fileDisplay.file = productFile;
        fileDisplay.file.fileState = FileState.ON_QUEUE;
        this.fileDisplayList.push(fileDisplay);
      }
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
              if (ret.fileState == FileState.MALICIOUS) {
                this.info = "Không thể tải lên file " + file.name + "có thể tệp chứa virus";
                this.openInfoModal();
              }
              if (index >= 1) {
                this.uploadFileIndex(index - 1, files);
              }
            }
          },
          (error) => {
            console.log(error);
            if (error.status === 400)
              fileDisplay.file.fileState = FileState.ERROR;
            this.info = "Không thể tải lên file " + file.name;
            this.openInfoModal();
            var index = -1;
            console.log(fileDisplay);
            for (let i = 0; i < this.fileDisplayList.length; i++) {
              if (file.name == this.fileDisplayList[i].file.name) {
                index = i;
                break;
              }
            }

            this.fileDisplayList = this.fileDisplayList.slice(0, index);
            this.cd.detectChanges();
            console.log(this.fileDisplayList.slice(0, 4));
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

  confirm = "";
  deleteFile(file: ProductFile) {
    if (this.product.approved != Status.NEW) {
      this.fileError = "Bạn cần click vào nút 'Tiếp tục cập nhật' để thực hiện hành động này";
      this.openFileSizeErrorModal();
      return;
    }

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
      this.productService.getLicenceByProductId(+productId).subscribe(
        (data) => {
          this.license = data;
        }
      )
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
          if (this.storageService.getAuthResponse().email != data.seller.email) {
            this.router.navigate(['error']);
          }
          this.product.price = Number.parseFloat(this.product.price.toFixed(1));
          this.product = data;
          console.log(this.product);
          this.getTagList();
          this.productDetails = this.product.details;
          if (this.product.category != null) {
            this.productCate = this.product.category.id
          } else {
            this.productCate = -1;
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
            this.nopayment = false;
            this.formattedPrice = this.getFormattedValue(this.product.price);
          } else {
            console.log("no payment");
            this.NoPaymentBtn.className += ' active';
            this.price = 0;
            this.nopayment = true;
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
            if (productFile.enabled) {
              var fileDisplay = new FileDisplay();
              fileDisplay.file = productFile;
              fileDisplay.file.fileState = FileState.STORED;
              this.fileDisplayList.push(fileDisplay);
            }
          }
          console.log(this.fileDisplayList);
        },

        (error) => {
          if (error.status === 404) {
            this.router.navigate(['error']);
          }
        }
      );
    }
  }
  getActiveVersion(productId: string | null) {
    if (productId) {
      this.manageProductService.getActiveVersionProductByIdAndSeller(+productId).subscribe(
        (data) => {
          if (this.storageService.getAuthResponse().email != data.seller.email) {
            this.router.navigate(['error']);
          }
          this.product = data;
          console.log(this.product);
          this.getTagList();
          this.productDetails = this.product.details;

          if (this.product.category != null) {
            this.productCate = this.product.category.id
          } else {
            this.productCate = -1;
          }
          this.product.price = Number.parseFloat(this.product.price.toFixed(1));

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
            if (productFile.enabled) {
              var fileDisplay = new FileDisplay();
              fileDisplay.file = productFile;
              fileDisplay.file.fileState = FileState.STORED;
              this.fileDisplayList.push(fileDisplay);
            }
          }
          console.log(this.fileDisplayList);

          if (this.product.price != 0) {
            this.nopayment = false;
            this.PayBtn.className += ' active';
            this.formattedPrice = this.getFormattedValue(this.product.price);
          } else {
            this.nopayment = true;
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
          if (error.status === 404) {
            this.router.navigate(['error']);
          }
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
    if (this.product.approved != Status.NEW) {

      this.fileError = "Bạn cần click vào nút 'Tiếp tục cập nhật' để thực hiện hành động này";
      this.openFileSizeErrorModal();
      return;
    }
    const file: File = $event.target.files[0];
    console.log(file.type);
    if (file.name.length > 100) {
      this.fileError = "Tên tệp từ 1 đến 100 kí tự";
      this.openFileSizeErrorModal();
      return;
    }
    if (file.size > 1024 * 1024 * 20) {
      this.fileError = "Độ lớn tối đa của ảnh: 20Mb";
      this.openFileSizeErrorModal();
      return;
    }
    if (file) {
      console.log(file.type);
      if (!this.checkFileType(file.type, IMAGE_EXTENSIONS)) {
        this.openFormatErrorModal();
        return;
      }

      else {
        //this.fileName = file.name;
        const formData = new FormData();
        formData.set("enctype", "multipart/form-data");
        formData.append("coverImage", file);
        formData.append("productId", this.product.id.toString());
        formData.append("version", this.product.version);
        const upload$ = this.manageProductService.uploadCoverImage(formData).subscribe(
          (data: string) => {
            console.log(this.product);
            this.CoverImageUploadBtn.value = "";
            this.product.coverImage = data;
            console.log(this.product);
            this.loadCoverImage();
          },
          (error: any) => {
            this.CoverImageUploadBtn.value = "";
            this.fileError = 'Tải lên hình ảnh không thành công';
            this.openFileSizeErrorModal();
          }
        )
      }
    }
    $event.target.reset;
  }

  getPreviewVideoSource(): string {
    if (this.product.previewVideo.id != -1 && this.product.previewVideo != null) {
      return "http://localhost:9000/public/serveMedia/video?source=" + this.product.previewVideo.source.replace(/\\/g, '/');
    }
    return "";
  }

  onPreviewVideoUpload($event: any) {
    if (this.product.approved != Status.NEW) {
      this.fileError = "Bạn cần click vào nút 'Tiếp tục cập nhật' để thực hiện hành động này";
      this.openFileSizeErrorModal();
      return;
    }

    const file: File = $event.target.files[0];
    if (file.name.length > 100 || file.name.length < 0) {
      this.fileError = "Tên tệp từ 1 đến 100 kí tự";
      this.openFileSizeErrorModal();
      return;
    }
    if (file.size > 1024 * 1024 * 200) {
      this.fileError = "Kích thước tối đa: 200Mb";
      this.openFileSizeErrorModal();
      return;
    }
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
            this.PreviewUploadVideoBtn.value = "";
            console.log(data);
            this.product.previewVideo = data;
          },
          (error) => {
            this.PreviewUploadVideoBtn.value = "";
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
    if (this.product.approved != Status.NEW) {
      this.fileError = "Bạn cần click vào nút 'Tiếp tục cập nhật' để thực hiện hành động này";
      this.openFileSizeErrorModal();
      return;
    }
    const files: File[] = $event.target.files;
    console.log($event.target.files.length);
    if (this.product.previewPictures.length + files.length > 7) {
      this.fileError = "Tối đa 7 ảnh";
      this.openFileSizeErrorModal();
    } else {
      if (files) {
        var valid = true;
        for (let i = 0; i < files.length; i++) {
          if (!this.checkFileType(files[i].type, IMAGE_EXTENSIONS)) {
            valid = false;
          }
          if (files[i].size > 1024 * 1024 * 20) {
            this.fileError = "Độ lớn tối đa của ảnh: 20Mb";
            this.openFileSizeErrorModal();
            return;
          }
          if (files[i].name.length > 100 || files[i].name.length < 0) {
            this.fileError = "Tên tệp từ 1 đến 100 kí tự";
            this.openFileSizeErrorModal();
            return;
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
                this.PreviewUploadImageBtn.value = "";
                this.product.previewPictures = data;
                this.percent = 'width:' + 100 / this.product.previewPictures.length + '%;';
              },
              (error) => {
                this.PreviewUploadImageBtn.value = "";
                this.fileError = 'Tải lên hình ảnh không thành công';
                this.openFileSizeErrorModal();
              }
            )
          }
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
    if (this.product.approved != Status.NEW) {
      this.fileError = "Bạn cần click vào nút 'Tiếp tục cập nhật' để thực hiện hành động này";
      this.openFileSizeErrorModal();
      return;
    }
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
      } else {
        this.info = "Chỉ có thể chọn tối đa 5 nhãn";
        this.openInfoModal();
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
  licenseInfo() {
    this.info = "Bạn không thể thay dổi giấy phép đã áp dụng cho sản phẩm này";
    this.openInfoModal();
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
  onPriceChange($event: any): void {
    const keyCode = $event.keyCode;
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
    return ('$' +
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
        this.nopayment = false;
        if (paid != null) {
          paid.setAttribute("style", "display:block;");
        }
        if (noPayment != null) {
          noPayment.setAttribute("style", "display:none;");
        }
      }
      if (clickedElement.className == "payment_mode_no_paid active") {
        // this.newProductForm.value.priceUnformated = '0';
        this.product.price = 0;
        this.nopayment = true;
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
    this.cd.detectChanges();
    this.modalService.dismissAll();
  }
  removeSpaces(text: string) {
    return text.replace(/\s+/g, ' ').trim();
  }

  saveProduct() {
    this.errors = [];
    this.newProductForm.markAllAsTouched;
    this.newProductForm.markAsDirty;
    if (this.product.name != null)
      this.product.name = this.removeSpaces(this.product.name);
    if (this.product.name == null || this.product.name == '') {
      this.errors.push(MSG100);
    } else {
      if (this.product.name.length > 30 || this.product.name.length < 3) {
        this.errors.push(MSG1001);
      }
    }

    if (this.newProductForm.value.price == null) {
      this.errors.push(MSG103);
    }
    if (this.fileDisplayList.length == 0) {
      this.errors.push(MSG104);
    }
    if (this.product.description != null)
      this.product.description = this.removeSpaces(this.product.description);
    if (this.product.description != null)
      if (this.product.description.length > 50) {
        const MSG1002 = 'Độ dài tối đa của mô tả là 50';
        this.errors.push(MSG1002);
      }
    if (this.productDetails != null)
      this.productDetails = this.removeSpaces(this.productDetails);
    if (this.productDetails != null)
      if (this.productDetails.length > 1000) {
        this.errors.push(MSG1003);
      }

    if (this.product.price > 1000) {
      this.errors.push();
    }

    this.InstructionDetails.value = this.removeSpaces(this.InstructionDetails.value);
    if (this.InstructionDetails.value.length > 200) {
      this.errors.push(MSG1004);
    }
    if (this.productCate == -1 || this.productCate == 0) {
      this.errors.push(MSG102);
    }

    if (this.Paid.style.display === 'none') {
      this.price = 0;
    } else {

      var priceString = this.formattedPrice.replace(",", "");
      var priceString = priceString.replace("$", "");
      this.price = Number.parseInt(priceString);
      if (this.price > 1000) {
        this.errors.push("Số tiền tối đa là 1000$");
      }

      if (this.price < 2) {
        this.errors.push("Số tiền tối thiểu là 2$");
      }

      if (this.price % 1 != 0) {
        this.errors.push('Số tiền là bội số của 1');
      }
    }





    if (this.errors.length == 0) {

      this.newProductForm.controls.id.setValue(this.product.id);
      this.newProductForm.controls.name.setValue(this.product.name);
      this.newProductForm.controls.tags.setValue(this.product.tags);
      this.newProductForm.controls.category.setValue(this.typeList[this.productCate - 1]);
      this.newProductForm.controls.details.setValue(this.productDetails);
      this.newProductForm.controls.price.setValue(this.product.price.toString());
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
    this.NewVersionSpecify.value = this.NewVersionSpecify.value.replace(/\s+/g, '').trim();
    if (this.NewVersionSpecify.value.length < 3 || this.NewVersionSpecify.value.length > 30) {
      this.fileError = "Tên phiên bản có độ dài từ 3 đến 30 kí tự";
    }
    if (this.NewVersionSpecify.value.length == 0) {
      this.fileError = 'Tên phiên bản không được trống';
      this.openFileSizeErrorModal();
      return;
    } else
      if (this.NewVersionSpecify.value.endsWith(".")) {
        this.fileError = 'Tên phiên bản không thể kết thúc với "."';
        this.openFileSizeErrorModal();
        return;
      } else
        if (this.versions.includes(this.NewVersionSpecify.value)) {
          this.fileError = 'Tên phiên bản đã tồn tại';
          this.openFileSizeErrorModal();
          return;
        } else
          if (this.versions.length >= 10) {
            this.fileError = 'Không thể có nhiều hơn 10 phiên bản';
            this.openFileSizeErrorModal();
            return;
          }
          else {
            this.productService.createNewVersion(this.product, this.NewVersionSpecify.value).subscribe(
              data => {
                console.log(data);
                window.location.href = 'http://localhost:4200/product/update/' + this.product.id + '/' + this.NewVersionSpecify.value;
              },
              error => {
                if (error.status === 409) {
                  this.fileError = 'Tên phiên bản đã tồn tại';

                } else if (error.error.messages.includes('Version name length is 1 to 30')) {
                  this.fileError = "Tên phiên bản có độ dài từ 1 đến 30 kí tự";
                } else if (error.error.messages.includes('Version name cannot contains spaces')) {
                  this.fileError = "Tên phiên bản không bao gồm khoảng trống";
                } else if (error.error.messages.includes('Không thể có nhiều hơn 10 phiên bản')) {
                  this.fileError = "Tên phiên bản không bao gồm khoảng trống";
                }
                this.openFileSizeErrorModal();
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
    if (this.license != null) {
      return this.license.details;
    } else {
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
    this.modalService.open(this.specifyVersionModal, { centered: true, size: "xl" });
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

  get PreviewUploadImageBtn() {
    return document.getElementById('previewUploadImage') as HTMLInputElement;
  }

  get PreviewUploadVideoBtn() {
    return document.getElementById('previewUploadVideo') as HTMLInputElement;
  }

  get CoverImageUploadBtn() {
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


  formattedPrice = '$2';

  updatePrice(value: any) {
    this.priceError();
    this.formattedPrice = this.getFormattedValue(value);
  }


  exclusiveKey = [13, 32, 190];
  exclusiveKey2 = [8, 190, 189]


  checkInput($event: any) {
    if (!this.exclusiveKey.includes($event.keyCode) && ($event.keyCode < 48 || $event.keyCode > 57)) {
      $event.preventDefault();
    }
    this.priceErrorr = [];
    console.log($event.keyCode);
  }

  version = '';
  versionError: string[] = [];

  checkInputVersion($event: any) {
    if (!this.exclusiveKey2.includes($event.keyCode)
      && ($event.keyCode < 48 || ($event.keyCode > 57 && $event.keyCode < 65) || $event.keyCode > 90)) {
      $event.preventDefault();
    }

    if (($event.shiftKey && $event.keyCode >= 48 && $event.keyCode <= 57) || (($event.ctrlKey && $event.keyCode == 67) || ($event.ctrlKey && $event.keyCode == 86))) {
      $event.preventDefault();
    }
    let maxLength = 30;
    console.log($event.keyCode);
    let value = $event.target.value;
    if (value.length + 1 > maxLength && $event.keyCode != 8) {
      $event.preventDefault();
    }
    this.versionError = [];

  }

  priceErrorr: string[] = [];

  priceError() {
    let intValue = Number.parseInt(this.formattedPrice.replace(',', "").replace('$', ""));

    if (intValue < 2) {
      this.priceErrorr.push("Số tiền tối thiểu là 2$")
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

        if (data == true)
          this.product.activeVersion = version.version;
        else {
          this.fileError = "Phiên bản mục tiêu không có tệp nào để download, không thể sử dụng phiên bản này";
          this.openFileSizeErrorModal();
        }
      },
      (error) => {
        this.fileError = "Thay đổi phiên bản sản phẩm không thành công";
        this.openFileSizeErrorModal();
      });
  }
  nopayment = true;
  dateFormat(date1: Date): string {
    var date: Date = new Date(date1.toString());
    const day = date.getDate();
    const month = date.getMonth() + 1;
    const year = date.getFullYear();
    const hours = date.getHours();
    const minutes = date.getMinutes();
    const seconds = date.getSeconds();
    return `${day}-${month}-${year} ${hours}:${minutes}:${seconds}`;
  }

  getApproved(status: Status): string {
    switch (status) {
      case Status.NEW:
        return "Chưa kiểm duyệt";
      case Status.PENDING:
        return "Chờ kiểm duyệt";
      case Status.APPROVED:
        return "Đã kiểm duyệt";
      case Status.REJECTED:
        return "Bị từ chối";
    }
  }

  setPending() {
    this.errors = [];
    this.newProductForm.markAllAsTouched;
    this.newProductForm.markAsDirty;
    if (this.product.name != null)
      this.product.name = this.removeSpaces(this.product.name);

    if (this.product.name == null || this.product.name == '') {
      this.errors.push(MSG100);
    } else {
      if (this.product.name.length > 30 || this.product.name.length < 3) {
        this.errors.push(MSG1001);
      }
    }

    if (this.newProductForm.value.price == null) {
      this.errors.push(MSG103);
    }
    if (this.fileDisplayList.length == 0) {
      this.errors.push(MSG104);
    }
    if (this.product.description != null)
      this.product.description = this.removeSpaces(this.product.description);
    if (this.product.description != null)
      if (this.product.description.length > 50) {
        const MSG1002 = 'Độ dài tối đa của mô tả là 50';
        this.errors.push(MSG1002);
      }
    if (this.productDetails != null)
      this.productDetails = this.removeSpaces(this.productDetails);
    if (this.productDetails != null)
      if (this.productDetails.length > 1000) {
        this.errors.push(MSG1003);
      }

    if (this.product.price > 1000) {
      this.errors.push();
    }

    this.InstructionDetails.value = this.removeSpaces(this.InstructionDetails.value);
    if (this.InstructionDetails.value.length > 200) {
      this.errors.push(MSG1004);
    }
    if (this.productCate == -1 || this.productCate == 0) {
      this.errors.push(MSG102);
    }

    if (this.Paid.style.display === 'none') {
      this.product.price = 0;
    } else {

      var priceString = this.formattedPrice.replace(",", "");
      var priceString = priceString.replace("$", "");
      this.price = Number.parseInt(priceString);
      if (this.product.price > 1000) {
        this.errors.push("Số tiền tối đa là 1000$");
      }

      if (this.product.price < 2) {
        this.errors.push("Số tiền tối thiểu là 2$");
      }
      console.log(this.product.price * 10 % 1);
      if ((this.product.price * 10 % 1) != 0) {
        this.errors.push('Số tiền là bội số của 0.1');
      }
    }

    console.log(this.product.price);

    if (this.errors.length == 0) {

      this.newProductForm.controls.id.setValue(this.product.id);
      this.newProductForm.controls.name.setValue(this.product.name);
      this.newProductForm.controls.tags.setValue(this.product.tags);
      this.newProductForm.controls.category.setValue(this.typeList[this.productCate - 1]);
      this.newProductForm.controls.details.setValue(this.productDetails);
      this.newProductForm.controls.price.setValue(this.product.price.toString());
      this.newProductForm.controls.description.setValue(this.product.description);
      this.newProductForm.controls.version.setValue(this.product.version);


      this.manageProductService.updateProduct(this.newProductForm.value, this.InstructionDetails.value).subscribe(
        (data) => {
          this.product = data;
          this.verifyProduct();
        },
        (error) => {
          this.fileError = "Cập nhật không thành công";
          this.openFileSizeErrorModal();
          console.log(error);
        }
      )
    } else { this.openVerticallyCentered(); }
  }

  public setNew() {
    this.cancelVerifyProduct();
  }

  public get ProductStatus(): typeof Status {
    return Status;
  }

  public verifyProduct() {
    this.productService.verifyProduct(this.product.id, this.product.version).subscribe(
      data => {
        this.product = data; this.product.price = Number.parseFloat(this.product.price.toFixed(1));
        this.getAllVersionOfProduct(this.product.id);
        this.info = "Bản cập nhật của bạn đang được xử lý";
        this.openInfoModal();
      },
      error => {
        console.log(error);
      });
  }

  public cancelVerifyProduct() {
    this.productService.cancelVerifyProduct(this.product.id, this.product.version).subscribe(
      data => {
        this.product = data; this.product.price = Number.parseFloat(this.product.price.toFixed(1));
        this.getAllVersionOfProduct(this.product.id);
        this.info = "Bạn có thể tiếp tục cập nhật phiên bản này";
        this.openInfoModal();
      },
      error => {
        console.log(error);
      });
  }

  backToShop() {
    this.router.navigate(['collection/', this.product.seller.id]);
  }
  license: License;
  openLicense() {
    if (this.product.approved != Status.NEW) {
      this.info = "Bạn cần click vào nút 'Tiếp tục cập nhật để thực hiện hành động này'";
      this.openInfoModal();
      return;
    }
    if (this.product.approvedDate != null) {
      this.info = "Chỉ có thể cập nhật giấy phép 1 lần";
      this.openInfoModal();
      return;
    }
    const data = {
      productId: this.product.id,
    }
    const dialogRef = this.dialog.open(ApplyLicenseComponent, {
      // height: '57%',
      width: '60%',
      data: data,
      panelClass: 'my-dialog-container'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      setTimeout(() => this.refresh(), 300)
    });
  }

  refresh() {
    this.productService.getLicenceByProductId(this.product.id).subscribe(
      (data) => {
        this.license = data;
      }
    );
  }

  getLicenseName(): string {
    if (this.license != null)
      return this.license.name;
    else
      return '';
  }

  get CateDetails() {
    if (this.productCate > 0) {
      var details = '';
      for (let i = 0; i < this.typeList.length; i++) {
        if (this.typeList[i].id == this.productCate) {
          details = this.typeList[i].description;
        }
      }
      return details;
    }
    else {
      return '';
    }
  }

}

