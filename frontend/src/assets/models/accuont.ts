export enum AccountType {
    SAVINGS = 'SAVINGS',
    CHECKING = 'CHECKING'
}

export enum AccountStatus {
    ACTIVE = 'ACTIVE',
    INACTIVE = 'INACTIVE',
    BLOCKED = 'BLOCKED'
}


export interface Account {
    accountId: string
    customerId: string
    accountNumber: string
    accountType: string
    balance: number
    currency: string
    status: AccountStatus
    createdAt: Date
    updatedAt: Date
}