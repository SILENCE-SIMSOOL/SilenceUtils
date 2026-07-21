# Terminal Solver

## How do terminal GUIs work?

When you click the correct slot, every terminal except the Melody Terminal appears to replace the clicked item as if it has been solved.
In reality, the item is not replaced. Instead, the game opens an entirely new GUI with the updated state.

## How does latency affect terminals?

Let's assume you have 200 ms of latency.
When you click the correct slot in a terminal, the updated GUI is only opened after 200 ms.
During those 200 ms, any additional clicks or actions are ignored because the new GUI has not yet been received.

## What does Fix Latency improve?

Fix Latency improves responsiveness by calculating whether a slot is clickable on the client side.

When a slot is considered clickable, its button is hidden on the Terminal Screen after you click it. If the slot cannot be clicked yet, further clicks are blocked.

As a result, terminal interactions feel instantaneous. While this does not actually reduce your network latency, it provides immediate client-side feedback on whether your click has been registered, eliminating the delay before the server responds.

This also prevents the common "lagback" effect where already-clicked items temporarily reappear, making terminal solving much smoother.

## Useful feature for the Melody Terminal

The Melody Terminal includes an exclusive anti-misclick feature that is still unique to SilenceUtils.

It prevents Wrong Clicks after pressing a button, so you can safely spam clicks without making mistakes.

This makes legitimate Insta 4 Skip much easier to perform.
