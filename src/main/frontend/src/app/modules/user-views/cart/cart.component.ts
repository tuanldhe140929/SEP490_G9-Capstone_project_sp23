import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Cart, Change, Type } from 'src/app/dtos/Cart';
import { CartItem } from 'src/app/dtos/CartItem';
import { Product } from 'src/app/dtos/Product';
import { Transaction, TransactionStatus } from 'src/app/dtos/Transaction';
import { CartService } from 'src/app/services/cart.service';
import { ProductService } from 'src/app/services/product.service';
import { TransactionService } from 'src/app/services/transaction.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
})
export class CartComponent implements OnInit {

  @ViewChild('infoModal', { static: false }) private infoModal: any;
  @ViewChild('change', { static: false }) private change: any;
  isPopupShown = false;
  isLoading = false;
  info = "";
  cart: Cart = new Cart;
  cartItemList: CartItem[] = [];
  itemname: any;
  itemprice: any;
  transaction: Transaction = new Transaction;
  updated: Change[] = [];
  removed: Change[] = [];
  convertRate: number;
  constructor(private cartService: CartService,
    private transactionService: TransactionService,
    private modalService: NgbModal,
    private router: Router,
    private productService: ProductService) {

  }
  ngOnInit(): void {
    this.getVNDPrice();
    this.getAllCartItem();
  }
  public getAllCartItem() {
    this.cartService.getAllProduct().subscribe(
      data => {
      
       
       
        for (let i = 0; i < data.items.length; i++) {
          data.items[i].product.price = Number.parseFloat(data.items[i].product.price.toFixed(1));
        }
        this.cart = data;
        this.cartItemList = data.items;
        if (this.cart.changes.length != 0) {
          for (let i = 0; i < this.cart.changes.length; i++) {
            if (this.cart.changes[i].type == Type.REMOVED) {
              console.log(this.cart.changes[i]);
              this.removed.push(this.cart.changes[i]);
            }
            if(this.cart.changes[i].type == Type.UPDATED) {
              this.updated.push(this.cart.changes[i]);
            } 
            this.openChangeModal();

            this.getAllCartItem();
          }
         
        }
      },
      (error) => {
        console.log(error);
      }
    )
  }
  public getItemCoverImageSrc(cartItem: CartItem) {
    if (cartItem != null && cartItem.product.id != -1 && cartItem.product.coverImage != null) {
      return 'http://localhost:9000/public/serveMedia/image?source=' + cartItem.product.coverImage.replace(/\\/g, '/');
    }
    else {
      return "assets/images/noimage.png";
    }
  }

  public RemoveItem(cartItem: CartItem): void {

    this.cartService.removeItem(cartItem).subscribe(
      (data) => {
      
        for (let i = 0; i < data.items.length; i++) {
          data.items[i].product.price = Number.parseFloat(data.items[i].product.price.toFixed(1));
        }  this.cart = data;
        this.cartItemList = data.items;
      },
      (error) => {
        if (error.error.messages.includes('Cart is currently checking out')) {
          this.info = 'Không thể xóa sản phẩm do đang có giao dịch đang xảy ra';
          this.openInfoModal();
        }
        if (error.error.messages.includes('Cart does not have product with id:'+cartItem.product.id)) {
          this.info = 'Sản phẩm này không có trong giỏ hàng';
          this.openInfoModal();
          this.getAllCartItem();
        }
      }
    )
    console.log('xóa thành công ' + cartItem.product.id)
    this.RemoveItem === null
  }
  showPopup() {
    if (this.cartItemList.length == 0) {
      this.info = 'Không có sản phẩm nào trong giỏ hàng';
      this.openInfoModal();
      return;
    }
    this.isPopupShown = true;
  }
  
  hidePopup() {
    this.isCheckboxChecked = false;
    this.isPopupShown = false;
  }

  public showPopups(): void {
    this.showPopup();
    const userPolicyCheckbox = document.getElementsByName('user-policy')[0] as HTMLInputElement;
    const continueButton = document.getElementsByName('continue')[0] as HTMLButtonElement;
    if(userPolicyCheckbox!=null && continueButton!=null){
      userPolicyCheckbox.checked= false;
      continueButton.disabled=true;
      
      // Add event listener to the checkbox element
  
  
      userPolicyCheckbox.addEventListener('change', function() {
        if (!userPolicyCheckbox.checked) {
          continueButton.disabled = true;
        } else {
          continueButton.disabled = false;
          
        }
      });
      
      // Set initial disabled state of continueButton based on checkbox state
      if (!userPolicyCheckbox.checked) {
        continueButton.disabled = true;
      } else {
        continueButton.disabled = false;
      }
    }
    
  }
  public checkout(): void {
    this.hidePopup();
    
    this.isLoading = true;
    this.transactionService.purchase(this.cart.id).subscribe(
      data => {
        this.isLoading = false;
        this.transaction = data;
        if (this.transaction.change) {
          this.removed = [];
          this.updated = [];
          if (this.transaction.cart.changes.length != 0) {
            for (let i = 0; i < this.transaction.cart.changes.length; i++) {
              if (this.transaction.cart.changes[i].type == Type.REMOVED) {
                console.log(this.cart.changes[i]);
                this.removed.push(this.transaction.cart.changes[i]);
              }
              if (this.transaction.cart.changes[i].type == Type.UPDATED) {
                this.updated.push(this.transaction.cart.changes[i]);
              }
            }
              this.openChangeModal();
            }
          this.getAllCartItem();
        }
        if (this.transaction.amount > 0) {
          var strWindowFeatures = "location=yes,height=570,width=520,scrollbars=yes,status=yes";
          var URL = data.approvalUrl;
          window.open(URL, "_blank", strWindowFeatures);
        }

        this.fetchTransactionStatus();
      },
      error => {this.isLoading = false;

        if(error.error.messages.includes('Another transaction is processing')){
          this.info = "Đang có giao dịch diễn ra";
          
        }
        else{
        this.info = "Không thể thực hiện hành động";}
        this.openInfoModal();
      }
    )
  }
  fetchTransactionStatus() {
    const checkStatus = () => {
      this.transactionService.checkTransactionStatus(this.transaction.id).subscribe(
        data => {
          this.isLoading = false;
          this.transaction.status = data;
          switch (data) {
            case TransactionStatus.CREATED:
              setTimeout(checkStatus, 3000);
              break;
            case TransactionStatus.PROCESSING:
              setTimeout(checkStatus, 3000);
              break;
            case TransactionStatus.APPROVED:
              setTimeout(checkStatus, 3000);
              break;
            case TransactionStatus.FAILED:
              this.getAllCartItem();
              this.info = "Thanh toán không thành công";
              this.openInfoModal();
              return;
            case TransactionStatus.COMPLETED:
              this.getAllCartItem();
              this.info = "Thanh toán thành công";
              this.openInfoModal();
              return;
            case TransactionStatus.CANCELED:
              this.info = "Đã hủy thanh toán";
              this.getAllCartItem();
              this.openInfoModal();
              return;
            case TransactionStatus.EXPIRED:
              this.info = "Giao dịch đã hết hạn";
              this.getAllCartItem();
              this.openInfoModal();
              return;
          }
          console.log(data);
          
        },
        error => {
          console.log(error);
        }
      )
    };
    checkStatus();
  }



  get TotalPrice() {
    return this.cart.totalPrice.toFixed(2);
  }

  get TotalPriceVND() {
    if (this.convertRate != null && this.cart.totalPrice != null)
      return Number(this.cart.totalPrice.toFixed(2)) * this.convertRate;
    else
      return 0;
  }
  openInfoModal() {
    this.modalService.open(this.infoModal, { centered: true });
  }

  openChangeModal() {
    this.modalService.open(this.change, { centered: true });
  }
  dismiss() {
    //window.location.reload();
    this.modalService.dismissAll();
  }

  toViewProduct(item: CartItem) {
    this.router.navigate(['products/' + item.product.id]);
  }

  get Fee() {
    const fee = Number.parseFloat(this.TotalPrice) / 10;
    return fee.toFixed(2);
  }

  get FeeVND() {
    if (this.TotalPrice != null && this.convertRate != null) {
      const fee = Number.parseFloat(this.TotalPrice) * this.convertRate / 10;
      return fee.toFixed(2);
    } else {
      return 0;
    }
  }

  get LastPrice() {
    const lastPrice = Number.parseFloat(this.Fee) + Number.parseFloat(this.TotalPrice);
    return lastPrice.toFixed(2);
  }
  
  get LastPriceVND() {
    if(this.convertRate!=null){
    const lastPrice = (Number.parseFloat(this.Fee) + Number.parseFloat(this.TotalPrice))* this.convertRate;
    return lastPrice.toFixed(2);}
    else{
      return 0;
    }
  }

  getVNDPrice(){
    this.productService.getVNDRate().subscribe(
      data => {
        const convertData = data;
        this.convertRate  = Number(convertData.conversion_rate);
      }
    )
  }
  isCheckboxChecked = false;
  checkboxEvent($event: any) {
    this.isCheckboxChecked = $event.target.checked;
  }
}
