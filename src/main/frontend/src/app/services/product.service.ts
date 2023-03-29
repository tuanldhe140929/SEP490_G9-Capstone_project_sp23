import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, Observable, of, throwError } from 'rxjs';
import { License } from '../dtos/License';
import { Product } from '../dtos/Product';
import { ProductDTO } from '../dtos/ProductDTO';

const baseUrl = "http://localhost:9000/productDetails";
const baseUrlProduct = "http://localhost:9000/product";
@Injectable({
  providedIn: 'root'
})
export class ProductService {

  public getAllLicense():Observable<License[]>{
    return this.httpClient.get<License[]>(baseUrlProduct+'/getLicense');
  }

  getProductByIdAndVersion(productId: number, version: string): Observable<Product> {
    return this.httpClient.post<Product>(baseUrl + '/getByIdAndVersion', null, {
      params: {
        productId: productId,
        version: version
      }
    });
  }


  createNewVersion(product: Product, newVersion: string): Observable<Product> {
    return this.httpClient.post<Product>(baseUrl + '/createNewVersionV2', product, {
      params: {
        newVersion: newVersion
      }
    }
    )
  }

  deleteProduct(id: number): Observable<boolean> {
    return this.httpClient.delete<boolean>(baseUrlProduct + "/deleteProduct/" + id);//.pipe(catchError(x => this.handleException(x)));
  }
  createNewProduct(sellerid: number): Observable<ProductDTO> {
    return this.httpClient.post<ProductDTO>(baseUrlProduct+"/createNewProduct", null);
  }

  constructor(private httpClient: HttpClient,private router: Router) { }

  public getProductById(productId: number): Observable<Product> {
    return this.httpClient.get<Product>(baseUrl + "/getActiveVersion", {
      params: {
        productId: productId
      }
    });
  }

  public getProductByIdForDownload(productId: number): Observable<Product> {
    return this.httpClient.get<Product>(baseUrl + "/getActiveVersionForDownload", {
      params: {
        productId: productId
      }
    });
  }

  public getProductsCountBySellerId(sellerId: number): Observable<number> {
    return this.httpClient.get<number>("http://localhost:9000/product" + "/getProductsCountBySellerId", {
      params: {
        sellerId: sellerId
      }
    });
  }
  public GetProductDetails(sellerId: number): Observable<Product[]>{
    return this.httpClient.get<Product[]>(baseUrl+"/getProductDetails",{
      params:{
        sellerId: sellerId
      }
    })
  }

  getAllProducts(): Observable<any>{
    return this.httpClient.get<any>(baseUrl + '/getAllProducts');
  }

  getProductsByKeyword(keyword: string): Observable<any>{
    return this.httpClient.get<any>(baseUrl + "/getProductsByKeyword/" + keyword);
  }

  getFilteredProducts(keyword: string, categoryid: number, tagidlist: number[], min: number, max: number): Observable<any>{
    let params = new HttpParams().set('keyword', keyword).set('categoryid',categoryid).set('tagidlist',tagidlist.join(',')).set("min",min).set("max",max);
    return this.httpClient.get<any>("http://localhost:9000/productDetails/getProductsForSearching", {params});
  }

  getProductsBySellerForSeller(sellerid: number, keyword: string,categoryid: number, tagidlist: number[], min: number, max: number): Observable<any>{
    let params = new HttpParams().set('sellerid',sellerid).set('keyword', keyword).set('categoryid',categoryid).set('tagidlist',tagidlist.join(',')).set("min",min).set("max",max);
    return this.httpClient.get<any>("http://localhost:9000/productDetails/getProductsBySellerForSeller", {params});
  }

  getProductsBySellerForUser(sellerid: number, keyword: string,categoryid: number, tagidlist: number[], min: number, max: number): Observable<any>{
    let params = new HttpParams().set('sellerid',sellerid).set('keyword', keyword).set('categoryid',categoryid).set('tagidlist',tagidlist.join(',')).set("min",min).set("max",max);
    return this.httpClient.get<any>("http://localhost:9000/productDetails/getProductsBySellerForUser", {params});
  }

  getProductsByReportStatus(status: string): Observable<any>{
    const params = {
      status: status
    }
    return this.httpClient.get<any>("http://localhost:9000/productDetails/getProductsByReportStatus", {params})
  }

  getByApprovalStatus(status: string): Observable<any>{
    const params = {
      status: status
    }
    return this.httpClient.get<any>("http://localhost:9000/productDetails/getByApprovalStatus",{params})
  }



  updateApprovalStatus(productId: number, version: string, status: string): Observable<any>{
    const params = {
      productId: productId,
      version: version,
      status: status
    }
    return this.httpClient.put<any>("http://localhost:9000/productDetails/updateApprovalStatus",null,{params});
  }

  getByIdAndVersion(productId: number, version: string): Observable<any>{
    const params = {
      productId: productId,
      version: version
    }
    return this.httpClient.post<any>("http://localhost:9000/productDetails/getByIdAndVersion",null,{params});
  }

  getAllProductsLatestVers(){
    return this.httpClient.get<any>("http://localhost:9000/productDetails/allProductsLatestVers");
  }

  getActiveVersion(productId: number){
    const params = {
      productId: productId
    }
    return this.httpClient.get<any>("http://localhost:9000/productDetails/getActiveVersion",{params});
  }
  getAllProductForHome(){
    return this.httpClient.get<any>("http://localhost:9000/productDetails/GetAllProductForHomePage");
  }

  private handleException(err: HttpErrorResponse): Observable<any> {
    if (err.status === 403) {
      this.router.navigateByUrl('error');
    } else if (err.status === 0) {
      this.router.navigateByUrl('error');
    } else if (err.status === 404) {
      this.router.navigateByUrl('error');
    }
    return throwError(err);
  }

  public verifyProduct(id:number,version:string): Observable<Product> {
    return this.httpClient.post<Product>(baseUrl + '/verifyProduct', null, {
      params: {
        productId: id,
        version: version
      }
    });
  }

  public cancelVerifyProduct(id: number, version: string): Observable<Product> {
    return this.httpClient.post<Product>(baseUrl + '/cancelVerifyProduct', null, {
      params: {
        productId: id,
        version: version
      }
    });
  }
}
