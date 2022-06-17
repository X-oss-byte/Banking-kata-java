package com.optivem.kata.banking.core;

import com.optivem.kata.banking.core.domain.accounts.AccountNumberGenerator;
import com.optivem.kata.banking.core.domain.accounts.BankAccountRepository;
import com.optivem.kata.banking.core.domain.accounts.scoring.LinearFactorAggregator;
import com.optivem.kata.banking.core.domain.accounts.scoring.NameFactorCalculator;
import com.optivem.kata.banking.core.domain.accounts.scoring.ScoreCalculatorImpl;
import com.optivem.kata.banking.core.usecases.depositfunds.DepositFundsRequest;
import com.optivem.kata.banking.core.usecases.depositfunds.DepositFundsUseCase;
import com.optivem.kata.banking.core.usecases.openaccount.OpenAccountRequest;
import com.optivem.kata.banking.core.usecases.openaccount.OpenAccountResponse;
import com.optivem.kata.banking.core.usecases.openaccount.OpenAccountUseCase;
import com.optivem.kata.banking.core.usecases.viewaccount.ViewAccountRequest;
import com.optivem.kata.banking.core.usecases.viewaccount.ViewAccountResponse;
import com.optivem.kata.banking.core.usecases.viewaccount.ViewAccountUseCase;

public class Facade {

    private final DepositFundsUseCase depositFundsUseCase;
    private final OpenAccountUseCase openAccountUseCase;
    private final ViewAccountUseCase viewAccountUseCase;

    // TODO: VC: Perhaps server-side API facade? And server-side API facade?
    public Facade(AccountNumberGenerator accountNumberGenerator, BankAccountRepository bankAccountRepository) {
        var nameFactorCalculator = new NameFactorCalculator();
        var balanceFactorCalculator = new NameFactorCalculator(); // TODO: VC: Replace
        var timeFactorCalculator = new NameFactorCalculator(); // TODO: VC: Replace

        var factorAggregator = new LinearFactorAggregator(nameFactorCalculator, balanceFactorCalculator, timeFactorCalculator);
        var scoreCalculator = new ScoreCalculatorImpl(factorAggregator);

        this.depositFundsUseCase = new DepositFundsUseCase(bankAccountRepository);
        this.openAccountUseCase = new OpenAccountUseCase(accountNumberGenerator, bankAccountRepository);
        this.viewAccountUseCase = new ViewAccountUseCase(bankAccountRepository, scoreCalculator);
    }

    public void execute(DepositFundsRequest request) {
        depositFundsUseCase.handle(request);
    }

    public OpenAccountResponse execute(OpenAccountRequest request) {
        return openAccountUseCase.handle(request);
    }

    public ViewAccountResponse execute(ViewAccountRequest request) {
        return viewAccountUseCase.handle(request);
    }
}