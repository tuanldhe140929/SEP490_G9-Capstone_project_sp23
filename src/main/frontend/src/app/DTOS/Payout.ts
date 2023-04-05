import { Seller } from './Seller';
import { Transaction } from './Transaction';


export enum Status {
    COMPLETE = "COMPLETE", FAILED = "FAILED",
    CREATED = "CREATED", APPROVED = "APPROVED",
    CANCELED = "CANCELED", EXPIRED = "EXPIRED"
}
export class Payout {
    id: number;
    amount: number;
    createdDate: Date;
    lastModified: Date;
    status: Status;
    batchId: String;
    payoutFee: number;
    description: String;
    seller: Seller;
    transaction: Transaction;
    
    constructor() {
        this.id = -1;
        this.amount = -1;
        this.createdDate = new Date;
        this.lastModified = new Date;
        this.batchId = '';
        this.payoutFee = -1;
        this.description ='';
        this.seller = new Seller;
        this.transaction = new Transaction;
        this.status = Status.FAILED;

    }
}

