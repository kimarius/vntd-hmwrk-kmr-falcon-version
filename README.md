# Welcome to Vinted!

We, engineers at Vinted, are striving to provide the best solutions for business-side problems which scale, last and
require minimal maintenance. Our code is clean, well-tested and flexible and we are not afraid to touch legacy code,
re-evaluate it and rework it if necessary. We hope that our new colleagues appreciate the same values and stick to
best-practices.

## Backend Take-Home Task

In this take-home task we ask you to do a code review on a piece of code that attempts to solve a problem described in the [Problem](#the-problem)
section. Please provide extensive feedback on the code. Feel free to give your suggestions on code, test, and repository quality; advise on software design, abstractions, and concern separation; point out possible bugs, performance, scalability or security issues; think about trade-offs.

You are expected to not only demonstrate your software development knowledge and attention to detail but also your ability to give constructive and well argumented feedback.

This task should take at most one or two hours of your precious time.

Due to technical limitations of GitHub classrooms you need to perform such git commands after checking out the repo (to refresh git history):

```bash
git checkout origin/homework -b homework
git rebase -Xtheirs main
git push -f
```

Then open a pull request from the branch `homework` to the `main` branch. Once your pull request is open, you can proceed with the take home task and use the pull request for comments.

### The Problem

When a Vinted member purchases an item, it has to be shipped and Vinted provides various shipping options. Let's focus
on France. In France, it is allowed to ship ether via 'Mondial Relay' (MR in short) or 'La Poste' (LP). While 'La Poste'
provides usual courier delivery services, 'Mondial Relay' allows you to drop and pick up a shipment at drop-off and
pickup locations, thus being less convenient but cheaper and much more sustainable.

Each item, depending on its size gets an appropriate package size assigned to it:

* S - Small, a popular option to ship jewelry.
* M - Medium - clothes and similar items.
* L - Large - mostly shoes.

Shipping prices depend on a package size and a provider:

| Provider     | Package Size | Price  |
|--------------|--------------|--------|
| LP           | S            | 1.50 € |
| LP           | M            | 4.90 € |
| LP           | L            | 6.90 € |
| MR           | S            | 2 €    |
| MR           | M            | 3 €    |
| MR           | L            | 4 €    |

Usually, the whole shipping price is covered by the buyer but sometimes, in order to promote one or another shipping
provider, Vinted covers a part of the shipping price by offering discounts.

These are the shipping discount rules applicable to purchased items:

* All S size shipments should always match the lowest S package price among the providers.
* The third L shipment via LP should be free, but only once a calendar month.
* Accumulated discounts cannot exceed 10 € in a calendar month. If there are not enough funds to fully cover a
  discount this calendar month, it should be covered partially.

Members' transactions are listed in a file 'input.txt', each line contains: a date in the specified format, a package
size code and a provider code separated with a whitespaces:

```
2015-02-01 S MR
2015-02-02 S MR
2015-02-03 L LP
2015-02-05 S LP
2015-02-06 S MR
2015-02-06 L LP
2015-02-07 L MR
2015-02-08 M MR
2015-02-09 L LP
2015-02-10 L LP
2015-02-10 S MR
2015-02-10 S MR
2015-02-11 L LP
2015-02-12 M MR
2015-02-13 M LP
2015-02-15 S MR
2015-02-17 L LP
2015-02-17 S MR
2015-02-24 L LP
2015-03-01 S MR
```

The solution should output transactions and append a reduced shipment price with a shipment discount (or a '-' if there
is no discount).

```
2015-02-01 S MR 1.50 0.50
2015-02-02 S MR 1.50 0.50
2015-02-03 L LP 6.90 -
2015-02-05 S LP 1.50 -
2015-02-06 S MR 1.50 0.50
2015-02-06 L LP 6.90 -
2015-02-07 L MR 4.00 -
2015-02-08 M MR 3.00 -
2015-02-09 L LP 0.00 6.90
2015-02-10 L LP 6.90 -
2015-02-10 S MR 1.50 0.50
2015-02-10 S MR 1.50 0.50
2015-02-11 L LP 6.90 -
2015-02-12 M MR 3.00 -
2015-02-13 M LP 4.90 -
2015-02-15 S MR 1.50 0.50
2015-02-17 L LP 6.90 -
2015-02-17 S MR 1.90 0.10
2015-02-24 L LP 6.90 -
2015-03-01 S MR 1.50 0.50
```

### Requirements

* At Vinted we strive to write clean and simple and easy-to-maintain code, covered with unit tests. The solution should match this philosophy.
* The design should be flexible enough to allow adding new rules and modifying existing ones easily.
* Short documentation of design decisions and assumptions can be provided in the code itself or a README.
* Make sure your input data is loaded from a file (default name 'input.txt' is assumed).
* Make sure the solution outputs data to the screen (STDOUT) in a format described in the example above.

## Solution

Now that we are done with describing the take-home task, let us proceed to the reviewing part. The best place to start
is the [SOLUTION.md](SOLUTION.md) that will guide you through the decisions and the philosophy of the code author.

Have fun!

---
### Important
*Vinted, UAB collects, uses and stores your provided information to assess your suitability to enter into employment contract and suggest a job offer for you (we have the intention to enter into a contract with you (Art. 6 (1) (b) of GDPR). For more information on how Vinted, UAB uses your data and your rights, please see Vinted Job Applicant Privacy Policy available here: https://www.vinted.com/jobs/policy*

*By submitting the response to the given task, you hereby consent that Vinted, UAB shall have the right to reproduce and use the response that you submit for the purpose of its recruitment processes, which will be anonymised after your recruitment process.*
