# SPDX-License-Identifier: Apache-2.0
# Set a breakpoint on the $message assignment, then step over and inspect $total.
param([string]$Name = 'Consulo')

$numbers = @(2, 3, 5)
$total = ($numbers | Measure-Object -Sum).Sum
$message = "Hello, $Name! Total: $total"
Write-Output $message
